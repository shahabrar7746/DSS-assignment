package org.assignment.serviceimlementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assignment.entities.Customer;
import org.assignment.entities.Order;
import org.assignment.entities.Product;
import org.assignment.enums.OrderStatus;
import org.assignment.enums.ResponseStatus;
import org.assignment.exceptions.CustomerNotFoundException;
import org.assignment.exceptions.NoProductFoundException;
import org.assignment.exceptions.OrderNotFoundException;
import org.assignment.repository.interfaces.OrderRepository;
import org.assignment.repository.interfaces.ProductRepository;
import org.assignment.services.OrderService;
import org.assignment.util.Constants;
import org.assignment.util.MathUtil;
import org.assignment.util.Response;
import org.assignment.wrappers.ProductCountWrappers;
import org.hibernate.HibernateException;

import java.sql.SQLException;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class OrderServiceImplementation implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public Response fetchOrdersByCustomerAndOrderStatus(Customer customer, OrderStatus status) {
        List<Order> orders = orderRepository.getOrdersByStatusAndCustomer(customer, status);
        return orders.isEmpty() ? new Response(ResponseStatus.ERROR, null, "No Orders found")
                : new Response(orders);
    }

    @Override
    public Response getAllOrders(Customer customer) {
        Response response;
        try {
            response = new Response(orderRepository.getAllDeliveredOrders());
        } catch (SQLException e) {
            log.error("Some error occured while getting all delivered orders : ", e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        } catch (CustomerNotFoundException | OrderNotFoundException | NoProductFoundException e) {
            response = new Response(null, e.getLocalizedMessage());
        }
        return response;
    }

    @Override
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
                helperForCancelOrder(count, order.get());
            } catch (Exception e) {
                resp = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
            }
        }
        if (resp == null && order.isPresent()) {
            resp = new Response(count + " Order cancelled..");
        }
        return resp;
    }

    @Override
    public Response getAllDeliveredOrders() {
        Response response;
        try {
            response = new Response(orderRepository.getAllDeliveredOrders());
        } catch (SQLException e) {
            log.error("Some error occured while getting all delivered orders : ", e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        } catch (CustomerNotFoundException | OrderNotFoundException | NoProductFoundException e) {
            response = new Response(null, e.getLocalizedMessage());
        }
        return response;
    }

    private void helperForCancelOrder(int count, List<Order> orders) {
        int index = 0;
        while (index < count) {
            Order removedOrder = orders.get(index);
            removedOrder.setStatus(OrderStatus.CANCELED);
            orderRepository.updateOrder(removedOrder);
            index++;
        }
    }
}
