package org.assignment.serviceimlementation;

import jakarta.persistence.PersistenceException;

import lombok.extern.slf4j.Slf4j;
import org.assignment.entities.*;

import org.assignment.enums.Currency;
import org.assignment.enums.OrderStatus;
import org.assignment.enums.ResponseStatus;

import org.assignment.exceptions.CustomerNotFoundException;

import org.assignment.exceptions.NoProductFoundException;
import org.assignment.exceptions.OrderNotFoundException;

import org.assignment.repository.interfaces.CustomerRepository;
import org.assignment.repository.interfaces.OrderRepository;
import org.assignment.repository.interfaces.ProductRepository;
import org.assignment.repository.interfaces.SellerRepository;


import org.assignment.services.CustomerService;

import org.assignment.util.Constants;
import org.assignment.util.MathUtil;
import org.assignment.util.Response;
import org.assignment.wrappers.ProductCountWrappers;
import org.hibernate.HibernateException;

import java.sql.SQLException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CustomerServiceImplementation implements CustomerService {
    private CustomerRepository repository;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private SellerRepository sellerRepository;

    public CustomerServiceImplementation(CustomerRepository repository, OrderRepository orderRepository, ProductRepository productRepository, SellerRepository sellerRepository) {
        this.repository = repository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
    }


    @Override
    public Optional<Customer> findByEmail(String email) {
        Optional<Customer> optionalCustomer = Optional.empty();
        try {
            optionalCustomer = repository.fetchByEmail(email);
        } catch (SQLException | PersistenceException e) {
            log.error("Some error occured while finding customer by email, mail {}", email, e);
        }
        return optionalCustomer;
    }


    public Response getAllOrders(Customer customer) {
        List<Order> orders = new ArrayList<>();
        try {
            orders = orderRepository.getOrderByCustomer(customer);
        } catch (CustomerNotFoundException | NoProductFoundException | OrderNotFoundException e) {
            return new Response(null, e.getLocalizedMessage());
        } catch (SQLException | HibernateException e) {
            log.error("Some error occured while fetching orders for customer {} ", customer.getId(), e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return orders.isEmpty() ? new Response(null, "No order found") : new Response(orders);
    }

    public Response cancelOrder(int count, Customer customer, String productName, boolean multiple) {
        Optional<List<Order>> order = Optional.empty();
        Response resp = null;
        List<Order> orders = null;
        try {
            Optional<Product> product = productRepository.fetchProductByName(productName);
            if (product.isEmpty()) {
                resp = new Response(null, "Incorrect product name");
            } else {
                orders = orderRepository.fetchOrderByProductAndCustomer(product.get(), customer);
            }

        } catch (CustomerNotFoundException | NoProductFoundException e) {
            resp = new Response(null, e.getLocalizedMessage());
        } catch (SQLException | HibernateException e) {
            log.error("Some error occured while order cancelation for customer {} ", customer.getId(), e);
            resp = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        if (orders != null && !orders.isEmpty()) {
            order = Optional.of(orders);
        }
        if (resp == null && orders != null && orders.size() > 1 && count == 1 && !multiple) {
            resp = new Response(ResponseStatus.PROCESSING
                    , new ProductCountWrappers(productName, orders.size())
                    , null);
        }
        if (resp == null && order.isPresent()) {
            try {
                helperForCancelOrder(count, order.get());
            } catch (SQLException e) {
                resp = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
            }
        }
        if (resp == null && order.isPresent()) {
            resp = new Response(count + " Order cancelled..");
        }
        return resp;
    }


    private void helperForCancelOrder(int count, List<Order> orders) throws SQLException {
        int index = 0;
        while (index < count) {
            Order removedOrder = orders.get(index);
            removedOrder.setStatus(OrderStatus.CANCELED);
            orderRepository.updateOrder(removedOrder);
            index++;
        }
    }

    public Response intiateCart(Customer customer, String name, int quantity) {
        Response response;
        Optional<Product> product;
        Seller s;
        try {
            product = productRepository.fetchProductByName(name);
            s = sellerRepository.fetchById(1L).get();
            if (product.isPresent()) {
                final Product p = product.get();
                CartItems item = CartItems.builder()
                        .customer(customer)
                        .seller(s)
                        .product(p)
                        .quantity(quantity)
                        .build();
                if(customer.getCart().contains(item)){//edits the quantity if the product already exists in cart
                   int index = customer.getCart().indexOf(item);
                   CartItems storedItem = customer.getCart().get(index);
                   int oldQuantity = storedItem.getQuantity();
                   item.setQuantity(oldQuantity + quantity);
                   item.setId(storedItem.getId());
                   customer.getCart().set(index, item);
                   updateCustomerAndCart(customer);
                }else{//else inserts product into cart
                    customer.getCart().add(item);
                    repository.updateCustomer(customer);
                }

                response = new Response("Item added");
            } else {
                response = new Response(null, "Invalid product name please try again.");
            }
        } catch (SQLException e) {
            log.error("Some error occured while intiating cart for customer {} ", customer.getId(), e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }

        return response;
    }


    public Response removeFromCart(Customer customer, String pname) {
        Response response;
        List<CartItems> cartItems = customer.getCart();
        cartItems = cartItems
                .stream()
                .filter(c -> !c.getProduct()
                        .getName()
                        .equalsIgnoreCase(pname))
                .toList();
        if (cartItems.size() == customer.getCart().size()) {
            response = new Response(ResponseStatus.ERROR, null, "Could not find product");
        } else {
            customer.setCart(new ArrayList<>(cartItems));
            try {
                repository.updateCustomer(customer);
                response = new Response(ResponseStatus.SUCCESSFUL, "Removed successfully", null);
            } catch (SQLException | PersistenceException e) {
                log.error("Some error occured while removing items from cart for customer {} ", customer.getId(), e);
                return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
            }
        }
        return response;
    }

    public Response hasProduct(Customer customer, String productName) {
        boolean exists = customer
                .getCart()
                .stream()
                .anyMatch(item -> item
                        .getProduct()
                        .getName()
                        .equalsIgnoreCase(productName));
        return new Response(ResponseStatus.SUCCESSFUL, exists, null);
    }

    public Response orderFromCart(Customer customer, String name, int quantity, boolean edited) {
        Response resp = null;
        List<CartItems> items = customer
                .getCart()
                .stream()
                .filter(i -> i.getProduct().getName().equalsIgnoreCase(name))
                .toList();

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
        resp = resp == null ? new Response(ResponseStatus.SUCCESSFUL, items.size() + " items Ordered", null)
                : resp;
        return resp;
    }

    @Override
    public Response incrementQuantity(Customer customer, String productName, final int newQuantity) {
        Optional<CartItems> itemsOptional = findCartItemByName(customer, productName);
        Response response = null;
        if (itemsOptional.isEmpty()) {
            return new Response(ResponseStatus.ERROR, null, "Could not find product by the provided name");
        }
        CartItems item = itemsOptional.get();
        int currentQuantity = item.getQuantity();
        int updatedQuantity = newQuantity + currentQuantity;
        item.setQuantity(updatedQuantity);
        try {
            updateCustomerAndCart(customer);
            response = new Response(ResponseStatus.SUCCESSFUL, "Quantity updated !! ", null);
        } catch (Exception e) {
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
            log.error("Some error occured while incrementing quantity for customer ", customer.getId(), e);
        }
        return response;
    }

    @Override
    public Response decrementQuantity(Customer customer, String productName, int newQuantity) {
        Optional<CartItems> itemsOptional = findCartItemByName(customer, productName);
        Response response = null;
        if (itemsOptional.isEmpty()) {
            return new Response(ResponseStatus.ERROR, null, "Could not find product by the provided name");
        }
        CartItems item = itemsOptional.get();
        if(item.getQuantity() < newQuantity)
        {
            return new Response(ResponseStatus.ERROR, null, "Quantity must be lesser than existing quantity of product");
        }
        if(item.getQuantity() == newQuantity)
        {
            return removeFromCart(customer, productName);
        }
        int currentQuantity = item.getQuantity();
        int updatedQuantity = currentQuantity - newQuantity;
        item.setQuantity(updatedQuantity);
        try {
            updateCustomerAndCart(customer);
            response = new Response(ResponseStatus.SUCCESSFUL, "Quantity updated !! ", null);
        } catch (Exception e) {
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
            log.error("Some error occured while incrementing quantity for customer ", customer.getId(), e);
        }
        return response;
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

    private void updateCustomerAndCart(Customer customer) throws SQLException {
        customer.getCart()
                .forEach(cartItems -> {
                    cartItems.setCustomer(customer);
                });
        repository.updateCustomer(customer);
    }
}
