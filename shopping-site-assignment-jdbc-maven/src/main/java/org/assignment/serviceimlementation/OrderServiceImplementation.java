package org.assignment.serviceimlementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assignment.entities.CartItems;
import org.assignment.entities.User;
import org.assignment.entities.Order;
import org.assignment.entities.Product;
import org.assignment.enums.Currency;
import org.assignment.enums.OrderStatus;
import org.assignment.enums.ResponseStatus;
import org.assignment.exceptions.CustomerNotFoundException;
import org.assignment.exceptions.NoProductFoundException;
import org.assignment.exceptions.OrderNotFoundException;
import org.assignment.repository.interfaces.OrderRepository;
import org.assignment.repository.interfaces.ProductRepository;

import org.assignment.services.UserService;
import org.assignment.services.OrderService;
import org.assignment.util.CartUtil;
import org.assignment.util.Constants;
import org.assignment.util.MathUtil;
import org.assignment.util.Response;
import org.assignment.wrappers.ProductCountWrappers;
import org.hibernate.HibernateException;

import java.sql.SQLException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class OrderServiceImplementation implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    @Override
    public Response fetchOrdersByCustomerAndOrderStatus(User user, OrderStatus status) {
        List<Order> orders = orderRepository.getOrdersByStatusAndCustomer(user, status);
        return orders.isEmpty() ? new Response(ResponseStatus.ERROR, null, "No Orders found")
                : new Response(ResponseStatus.SUCCESSFUL, orders, null);
    }

    @Override
    public Response cancelOrder(int count, User user, String productName, boolean multiple) {
        Optional<List<Order>> order = Optional.empty();
        Response resp = null;
        List<Order> orders = null;
        try {
            Optional<Product> product = productRepository.fetchProductByName(productName);
            if (product.isEmpty()) {
                resp = new Response(ResponseStatus.ERROR, null, "Incorrect product name");
            } else {
                orders = orderRepository.fetchOrderByProductAndCustomer(product.get(), user);
            }

        } catch (CustomerNotFoundException | NoProductFoundException e) {
            resp = new Response(ResponseStatus.ERROR, null, e.getLocalizedMessage());
        } catch (SQLException | HibernateException e) {
            log.error("Some error occured while order cancelation for user {} ", user.getId(), e);
            resp = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        if (orders != null && !orders.isEmpty()) {
            order = Optional.of(orders);
        }
        if (resp == null && orders != null && orders.size() > 1 && count == 1 && !multiple) {
            double price = orders.getFirst().getPrice();
            int quantity = orders.getFirst().getQuantity();
            double total = MathUtil.getTotalFromPriceAndQuantity(price, quantity);
            int numberOfOrders = orders.size();//number of orders of same product.
            resp = new Response(ResponseStatus.PROCESSING
                    , new ProductCountWrappers(productName, numberOfOrders, price, total)
                    , null);
        }
        if (resp == null && order.isPresent()) {
            try {
                helperForCancelOrderAndUpdateStockOfProduct(count, order.get());
            } catch (Exception e) {
                resp = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
            }
        }
        if (resp == null && order.isPresent()) {
            resp = new Response(ResponseStatus.SUCCESSFUL, count + " Order cancelled..", null);
        }
        return resp;
    }

    @Override
    public Response getAllOrdersByCustomer(User user) {
        Response response;
        try {
            response = new Response(ResponseStatus.SUCCESSFUL, orderRepository.getOrderByCustomer(user), null);
        } catch (SQLException e) {
            log.error("Some error occured while getting all delivered orders : ", e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        } catch (CustomerNotFoundException | OrderNotFoundException | NoProductFoundException e) {
            response = new Response(ResponseStatus.ERROR, null, e.getLocalizedMessage());
        }
        return response;
    }

    @Override
    public Response orderCart(User user) {
        if (user.getCart().isEmpty()) {
            return new Response(ResponseStatus.ERROR, null, "Cart is empty");
        }
        double total = CartUtil.getCartTotal(user.getCart());
        Currency currency = user.getCart().getFirst().getProduct().getCurrency();
        int cartSize = user.getCart().size();
        Response response;
        StringBuilder builder = new StringBuilder();
        for (CartItems item : user.getCart()) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                String productName = product.getName();
                int itemQuantity = item.getQuantity();
                builder
                        .append("Cannot order more stock for product ")
                        .append(productName)
                        .append(" with quantity of ")
                        .append(itemQuantity)
                        .append("\n");

            }
        }
        String message = builder.toString();
        if (!message.isBlank()) {
            return new Response(ResponseStatus.ERROR, null, message);
        }
        for (CartItems item : user.getCart()) {
            Product product = item.getProduct();

            Order order = Order.builder()
                    .orderedOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                    .user(user)
                    .price(total)
                    .status(OrderStatus.ORDERED)
                    .quantity(item.getQuantity())
                    .product(product)
                    .build();
            try {
                orderRepository.addOrder(order);
                int oldStock = product.getStock();
                product.setStock(oldStock - item.getQuantity());
                productRepository.updateProduct(product);
            } catch (Exception e) {
                log.error("Some error occured while placing orders from cart of user {} ", user.getId(), e);
                return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
            }
        }
        user.getCart().clear();
        response = userService.updateCustomerAndCart(user);
        response = response.getStatus() == ResponseStatus.ERROR ? response : new Response(ResponseStatus.SUCCESSFUL, cartSize + " items Ordered with total bill amount of " + total + currency.getSymbol(), null);
        return response;
    }

    private void helperForCancelOrderAndUpdateStockOfProduct(int count, List<Order> orders) {
        int index = 0;
        while (index < count) {
            Order removedOrder = orders.get(index);
            removedOrder.setStatus(OrderStatus.CANCELED);
            orderRepository.updateOrder(removedOrder);
            Product orderedProduct = removedOrder.getProduct();
            int currentStock = orderedProduct.getStock();
            orderedProduct.setStock(currentStock + removedOrder.getQuantity());
            productRepository.updateProduct(orderedProduct);
            index++;
        }
    }
}
