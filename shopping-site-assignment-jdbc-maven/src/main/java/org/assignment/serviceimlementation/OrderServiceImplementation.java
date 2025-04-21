package org.assignment.serviceimlementation;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assignment.entities.*;
import org.assignment.enums.Currency;
import org.assignment.enums.OrderStatus;
import org.assignment.enums.ResponseStatus;
import org.assignment.exceptions.CustomerNotFoundException;
import org.assignment.exceptions.NoProductFoundException;
import org.assignment.exceptions.OrderNotFoundException;
import org.assignment.repository.interfaces.InvoiceRepository;
import org.assignment.repository.interfaces.OrderRepository;
import org.assignment.repository.interfaces.ProductRepository;

import org.assignment.services.InvoiceService;
import org.assignment.services.UserService;
import org.assignment.services.OrderService;
import org.assignment.util.*;
import org.assignment.wrappers.ProductCountWrappers;
import org.hibernate.HibernateException;

import java.sql.SQLException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class OrderServiceImplementation implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceService invoiceService;

    @Override
    public Response fetchOrdersByCustomerAndOrderStatus(User user, OrderStatus status) {
        List<Order> orders = orderRepository.getOrdersByStatusAndCustomer(user, status);
        return orders.isEmpty() ? new Response(ResponseStatus.ERROR, null, "No Orders found")
                : new Response(ResponseStatus.SUCCESSFUL, orders, null);
    }

    @Override
    public Response cancelOrder(User user, int index) {

        try {
            List<Order> orders = orderRepository.getOrdersByStatusAndCustomer(user, OrderStatus.ORDERED)
                    .stream()
                    .sorted()
                    .collect(Collectors.toList());
            if (orders.size() <= index) {
                return new Response(ResponseStatus.ERROR, null, "Index out of limit");
            }
            Order orderTobeRemoved = orders.get(index);
            helperForCancelOrderAndUpdateStockOfProduct(orderTobeRemoved);
        } catch (Exception e) {
            log.error("Some error occured while fetching order based on  user {} ", user.getId(), e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return new Response(ResponseStatus.SUCCESSFUL, "Order cancelled", null);
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
        Response response;
        double total = CartUtil.getCartTotal(user.getCart());
        Currency currency = user.getCart().getFirst().getProduct().getCurrency();
        String message = canOrder(user.getCart());
        if (!message.isBlank()) {
            return new Response(ResponseStatus.ERROR, null, message);
        }
        List<OrderedProduct> productLst = user.getCart()
                .stream()
                .map(items -> {
                    OrderedProduct orderedProduct = OrderedProduct.builder()
                            .product(items.getProduct())
                            .quantity(items.getQuantity())
                            .productTotal(items.getQuantity() * items.getProduct().getPrice())
                            .build();
                    return orderedProduct;
                })
                .collect(Collectors.toList());

        Order order = Order.builder()
                .orderedOn(LocalDateTime.now())
                .user(user)
                .price(total)
                .status(OrderStatus.ORDERED)
                .orderedProducts(productLst)
                .build();
        try {

            Invoice invoice = invoiceService.generateInvoice(order);
            decreaseQuantity(productLst);
            StringBuilder messageBuilder = new StringBuilder(invoice.toString());
            messageBuilder.append("Your grand total amount is " + total + " " + currency.getSymbol());
            response = new Response(ResponseStatus.SUCCESSFUL, messageBuilder.toString(), null);
        } catch (Exception e) {
            log.error("Some error occured while placing orders from cart of user {} ", user.getId(), e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        user.getCart().clear();
        Response userUpdateSuccess = userService.updateCustomerAndCart(user);
        return userUpdateSuccess.getStatus() == ResponseStatus.ERROR ? userUpdateSuccess : response;
    }

    private void helperForCancelOrderAndUpdateStockOfProduct(Order removedOrder) {
        removedOrder.setStatus(OrderStatus.CANCELED);
        orderRepository.updateOrder(removedOrder);
        for (OrderedProduct orderedProduct : removedOrder.getOrderedProducts()) {
            Product product = orderedProduct.getProduct();
            int orderedQuantity = orderedProduct.getQuantity();
            int currentStock = product.getStock();
            product.setStock(currentStock + orderedQuantity);
            productRepository.updateProduct(product);
        }

    }

    private String canOrder(List<CartItems> cart) {
        StringBuilder builder = new StringBuilder();

        for (CartItems item : cart) {
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
        return message;
    }

    private void decreaseQuantity(List<OrderedProduct> orderedProducts) {
        for (OrderedProduct orderedProduct : orderedProducts) {
            Product product = orderedProduct.getProduct();
            int oldStock = product.getStock();
            product.setStock(oldStock - orderedProduct.getQuantity());
            productRepository.updateProduct(product);
        }
    }
}
