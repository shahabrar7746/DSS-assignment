package org.assignment.serviceimlementation;

import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.assignment.entities.*;
import org.assignment.enums.Currency;
import org.assignment.enums.OrderStatus;
import org.assignment.enums.ResponseStatus;
import org.assignment.repository.interfaces.UserRepository;
import org.assignment.repository.interfaces.OrderRepository;
import org.assignment.repository.interfaces.ProductRepository;
import org.assignment.services.CartService;
import org.assignment.util.CartUtil;
import org.assignment.util.Constants;
import org.assignment.util.MathUtil;
import org.assignment.util.Response;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class CartServiceImplementation implements CartService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public CartServiceImplementation(ProductRepository productRepository, OrderRepository orderRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Response intiateCart(User user, String name, int quantity) {
        Response response;
        Optional<Product> product;

        try {
            product = productRepository.fetchProductByName(name);
            if (product.isPresent() && product.get().getStock() >= quantity && product.get().getStock() > 0) {
                final Product p = product.get();
                CartItems item = CartItems.builder()
                        .user(user)
                        .product(p)
                        .quantity(quantity)
                        .build();
                if (user.getCart().contains(item)) {//edits the quantity if the product already exists in cart
                    int index = user.getCart().indexOf(item);
                    CartItems storedItem = user.getCart().get(index);
                    int oldQuantity = storedItem.getQuantity();
                    item.setQuantity(oldQuantity + quantity);
                    item.setId(storedItem.getId());
                    user.getCart().set(index, item);
                    updateCustomerAndCart(user);
                } else {//else inserts product into cart
                    user.getCart().add(item);
                    userRepository.updateCustomer(user);
                }

                double totalBill = CartUtil.getCartTotal(user.getCart());
                response = new Response(ResponseStatus.SUCCESSFUL, "Item added to the cart and your total bill is " + totalBill + item
                        .getProduct()
                        .getCurrency()
                        .getSymbol(), null);
            } else if (product.isPresent() && product.get().getStock() < quantity) {
                response = new Response(ResponseStatus.ERROR, null, "Provided quantity is greater than product stock");
            } else if (product.isEmpty()) {
                response = new Response(ResponseStatus.ERROR, null, "Invalid product name please try again.");
            } else {
                return new Response(ResponseStatus.ERROR, null, "Product is out off stock");
            }
        } catch (SQLException e) {
            log.error("Some error occured while intiating cart for user {} ", user.getId(), e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }

        return response;
    }

    @Override
    public Response removeFromCartByProductName(User user, String name) {
        Response response;
        List<CartItems> cartItems = user.getCart();
        cartItems = cartItems
                .stream()
                .filter(c -> !c.getProduct()
                        .getName()
                        .equalsIgnoreCase(name))
                .toList();
        if (cartItems.size() == user.getCart().size()) {
            response = new Response(ResponseStatus.ERROR, null, "Could not find product");
        } else {
            user.setCart(new ArrayList<>(cartItems));
            try {
                userRepository.updateCustomer(user);
                double totalBill = CartUtil.getCartTotal(user.getCart());
                response = new Response(ResponseStatus.SUCCESSFUL, "Removed successfully and now total bill is " + totalBill, null);
            } catch (SQLException | PersistenceException e) {
                log.error("Some error occured while removing items from cart for user {} ", user.getId(), e);
                return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
            }
        }
        return response;
    }

    @Override
    public Response removeFromCartByCartItemObject(User user, CartItems items) {
        user.getCart().remove(items);
        try {
            updateCustomerAndCart(user);
        } catch (Exception e) {
            log.error("Some error occured while removing items from cart for user {} ", user.getId(), e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }

        double totalBill = CartUtil.getCartTotal(user.getCart());
        return new Response(ResponseStatus.SUCCESSFUL, "Removed successfully and now total bill is " + totalBill, null);
    }

    @Override
    public Response orderFromCart(User user, String name, int quantity, boolean edited) {
        Response resp = null;
        List<CartItems> items = user
                .getCart()
                .stream()
                .filter(i -> i.getProduct().getName().equalsIgnoreCase(name))
                .toList();
        double totalBill = CartUtil.getCartTotal(items);
        Currency currency = Currency.INR;
        for (CartItems item : items) {
            if (!edited) {
                quantity = item.getQuantity();
            }
            currency = item.getProduct().getCurrency();
            double total = MathUtil.getTotalFromPriceAndQuantity(item.getProduct().getPrice(), quantity);
            Order order = Order.builder()
                    .orderedOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                    .user(user)
                    .price(total)
                    .status(OrderStatus.ORDERED)
                    .quantity(quantity)
                    .product(item.getProduct())
                    .build();
            try {
                orderRepository.addOrder(order);
            } catch (Exception e) {
                log.error("Some error occured while placing orders from cart of user {} ", user.getId(), e);
                resp = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
            }
        }
        try {
            List<CartItems> newCart = user
                    .getCart()
                    .stream()
                    .filter(i -> !i.getProduct().getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());
            user.setCart(newCart);
            updateCustomerAndCart(user);
        } catch (Exception e) {
            log.error("Some error occured while placing orders from cart of user {} ", user.getId(), e);
            resp = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }

        resp = resp == null ? new Response(ResponseStatus.SUCCESSFUL, items.size() + " items Ordered with total bill amount of " + totalBill + currency.getSymbol(), null)
                : resp;
        return resp;
    }

    @Override
    public Response changeQuantity(User user, CartItems item, int newQuantity) {
        if (newQuantity == 0) {
            return removeFromCartByCartItemObject(user, item);
        }
        Response response;
        if (item.getQuantity() == newQuantity) {
            return prepareResponseForChangeQuantity(item, user);
        }
        item.setQuantity(newQuantity);
        try {
            updateCustomerAndCart(user);
            response = prepareResponseForChangeQuantity(item, user);
        } catch (Exception e) {
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
            log.error("Some error occured while incrementing quantity for user {} ", user.getId(), e);
        }
        return response;
    }

    private Response prepareResponseForChangeQuantity(CartItems item, User user) {
        Response response;
        double totalBill = CartUtil.getCartTotal(user.getCart());
        response = new Response(ResponseStatus.SUCCESSFUL, "Quantity updated with total amount of " + totalBill +
                item
                        .getProduct()
                        .getCurrency()
                        .getSymbol(), null);
        return response;
    }

    @Override
    public Response getTotalBillFromCart(User user) {
        boolean isEmpty = user.getCart().isEmpty();
        String message = isEmpty ? "Cart is Empty" : "Your total amount of cart is " + CartUtil.getCartTotal(user.getCart()) + user
                .getCart()
                .getFirst()
                .getProduct()
                .getCurrency()
                .getSymbol();
        return isEmpty ? new Response(ResponseStatus.ERROR, null, message)
                : new Response(ResponseStatus.SUCCESSFUL, message, null);
    }

    private void updateCustomerAndCart(User user) throws SQLException {
        user.getCart()
                .forEach(cartItems -> cartItems.setUser(user));
        userRepository.updateCustomer(user);
    }

    @Override
    public Response findCartItemByName(User user, String productName) {
        Optional<CartItems> optionalCartItems = user
                .getCart()
                .stream()
                .filter(cartItems -> cartItems
                        .getProduct()
                        .getName()
                        .equalsIgnoreCase(productName))
                .findFirst();
        return new Response(ResponseStatus.SUCCESSFUL, optionalCartItems, null);
    }
}
