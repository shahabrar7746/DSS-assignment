package org.assignment.serviceimlementation;

import org.assignment.entities.Customer;
import org.assignment.entities.Order;
import org.assignment.enums.OrderStatus;
import org.assignment.enums.ResponseStatus;
import org.assignment.repository.interfaces.OrderRepository;
import org.assignment.services.OrderService;
import org.assignment.util.Response;

import java.util.ArrayList;
import java.util.List;

public class OrderServiceImplementation implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImplementation(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Response fetchOrdersByCustomerAndOrderStatus(Customer customer, OrderStatus status) {
        List<Order> orders = orderRepository.getOrdersByStatusAndCustomer(customer, status);
        return orders.isEmpty() ? new Response(ResponseStatus.ERROR, null, "No Orders found")
                : new Response(orders);
    }
}
