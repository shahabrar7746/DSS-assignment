package org.assignment.serviceimlementation;

import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.assignment.entities.*;
import org.assignment.enums.Currency;
import org.assignment.enums.OrderStatus;
import org.assignment.enums.ResponseStatus;
import org.assignment.repository.interfaces.CustomerRepository;
import org.assignment.repository.interfaces.OrderRepository;
import org.assignment.repository.interfaces.ProductRepository;
import org.assignment.repository.interfaces.SellerRepository;
import org.assignment.services.CartService;
import org.assignment.util.CartUtil;
import org.assignment.util.Constants;
import org.assignment.util.MathUtil;
import org.assignment.util.Response;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class CartServiceImplementation implements CartService {
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public CartServiceImplementation(ProductRepository productRepository, SellerRepository sellerRepository, OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Response intiateCart(Customer customer, String name, int quantity) {
        Response response;
        Optional<Product> product;
        Seller s;
        try {
            product = productRepository.fetchProductByName(name);
            s = sellerRepository.fetchById(1L).orElseGet(null);
            if (product.isPresent()) {
                final Product p = product.get();
                CartItems item = CartItems.builder()
                        .customer(customer)
                        .seller(s)
                        .product(p)
                        .quantity(quantity)
                        .build();
                if (customer.getCart().contains(item)) {//edits the quantity if the product already exists in cart
                    int index = customer.getCart().indexOf(item);
                    CartItems storedItem = customer.getCart().get(index);
                    int oldQuantity = storedItem.getQuantity();
                    item.setQuantity(oldQuantity + quantity);
                    item.setId(storedItem.getId());
                    customer.getCart().set(index, item);
                    updateCustomerAndCart(customer);
                } else {//else inserts product into cart
                    customer.getCart().add(item);
                    customerRepository.updateCustomer(customer);
                }

                double totalBill = CartUtil.getCartTotal(customer.getCart());
                response = new Response("Item added to the cart and your total bill is " + totalBill);
            } else {
                response = new Response(null, "Invalid product name please try again.");
            }
        } catch (SQLException e) {
            log.error("Some error occured while intiating cart for customer {} ", customer.getId(), e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }

        return response;
    }

    @Override
    public Response removeFromCart(Customer customer, String name) {
        Response response;
        List<CartItems> cartItems = customer.getCart();
        cartItems = cartItems
                .stream()
                .filter(c -> !c.getProduct()
                        .getName()
                        .equalsIgnoreCase(name))
                .toList();
        if (cartItems.size() == customer.getCart().size()) {
            response = new Response(ResponseStatus.ERROR, null, "Could not find product");
        } else {
            customer.setCart(new ArrayList<>(cartItems));
            try {
                customerRepository.updateCustomer(customer);
                double totalBill = CartUtil.getCartTotal(customer.getCart());
                response = new Response(ResponseStatus.SUCCESSFUL, "Removed successfully and now total bill is " + totalBill, null);
            } catch (SQLException | PersistenceException e) {
                log.error("Some error occured while removing items from cart for customer {} ", customer.getId(), e);
                return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
            }
        }
        return response;
    }

    @Override
    public Response orderFromCart(Customer customer, String name, int quantity, boolean edited) {
        Response resp = null;
        List<CartItems> items = customer
                .getCart()
                .stream()
                .filter(i -> i.getProduct().getName().equalsIgnoreCase(name))
                .toList();
        double totalBill = CartUtil.getCartTotal(items);
        for (CartItems item : items) {
            if (!edited) {
                quantity = item.getQuantity();
            }
            double total = MathUtil.getTotalFromPriceAndQuantity(item.getProduct().getPrice(), quantity);
            Order order = Order.builder()
                    .orderedOn(LocalDateTime.now())
                    .customer(customer)
                    .price(total)
                    .seller(item.getSeller())
                    .status(OrderStatus.ORDERED)
                    .currency(Currency.INR)
                    .quantity(quantity)
                    .product(item.getProduct())
                    .build();
            try {
                orderRepository.addOrder(order);
            } catch (Exception e) {
                log.error("Some error occured while placing orders from cart of customer {} ", customer.getId(), e);
                resp = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
            }
        }
        try {
            List<CartItems> newCart = customer
                    .getCart()
                    .stream()
                    .filter(i -> !i.getProduct().getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());
            customer.setCart(newCart);
            updateCustomerAndCart(customer);
        } catch (SQLException e) {
            log.error("Some error occured while placing orders from cart of customer {} ", customer.getId(), e);
            resp = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }

        resp = resp == null ? new Response(ResponseStatus.SUCCESSFUL, items.size() + " items Ordered with total bill amount of " + totalBill, null)
                : resp;
        return resp;
    }

    @Override
    public Response incrementQuantity(Customer customer, String productName, int newQuantity) {
        Optional<CartItems> itemsOptional = findCartItemByName(customer, productName);
        Response response;
        if (itemsOptional.isEmpty()) {
            return new Response(ResponseStatus.ERROR, null, "Could not find product by the provided name");
        }
        CartItems item = itemsOptional.get();
        int currentQuantity = item.getQuantity();
        int updatedQuantity = newQuantity + currentQuantity;
        item.setQuantity(updatedQuantity);
        try {
            updateCustomerAndCart(customer);
            double totalBill = CartUtil.getCartTotal(customer.getCart());
            response = new Response(ResponseStatus.SUCCESSFUL, "Quantity updated with total amount of " + totalBill, null);
        } catch (Exception e) {
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
            log.error("Some error occured while incrementing quantity for customer {} ", customer.getId(), e);
        }
        return response;
    }

    @Override
    public Response decrementQuantity(Customer customer, String productName, int newQuantity) {
        Optional<CartItems> itemsOptional = findCartItemByName(customer, productName);
        Response response;
        if (itemsOptional.isEmpty()) {
            return new Response(ResponseStatus.ERROR, null, "Could not find product by the provided name");
        }
        CartItems item = itemsOptional.get();
        if (item.getQuantity() < newQuantity) {
            return new Response(ResponseStatus.ERROR, null, "Quantity must be lesser than existing quantity of product");
        }
        if (item.getQuantity() == newQuantity) {
            return removeFromCart(customer, productName);
        }
        int currentQuantity = item.getQuantity();
        int updatedQuantity = currentQuantity - newQuantity;
        item.setQuantity(updatedQuantity);
        try {
            updateCustomerAndCart(customer);
            double totalBill = CartUtil.getCartTotal(customer.getCart());
            response = new Response(ResponseStatus.SUCCESSFUL, "Quantity updated with amount of " + totalBill, null);
        } catch (Exception e) {
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
            log.error("Some error occured while incrementing quantity for customer {}", customer.getId(), e);
        }
        return response;
    }

    @Override
    public Response getTotalBillFromCart(Customer customer) {
        boolean isEmpty = customer.getCart().isEmpty();
        return isEmpty ? new Response(ResponseStatus.ERROR, null, "Cart is empty")
                : new Response(ResponseStatus.SUCCESSFUL, CartUtil.getCartTotal(customer.getCart()), null);
    }

    private void updateCustomerAndCart(Customer customer) throws SQLException {
        customer.getCart()
                .forEach(cartItems -> cartItems.setCustomer(customer));
        customerRepository.updateCustomer(customer);
    }

    private Optional<CartItems> findCartItemByName(Customer customer, String productName) {
        return customer
                .getCart()
                .stream()
                .filter(cartItems -> cartItems
                        .getProduct()
                        .getName()
                        .equalsIgnoreCase(productName))
                .findFirst();
    }
}
