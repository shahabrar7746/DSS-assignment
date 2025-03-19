package service;

import entities.Order;
import utility.Response;

import java.util.List;

public interface OrderService {
    Response getOrder(int orderId);

    List<Order> getAllOrders();

    void placeOrder(Order order);

    void simulateOrderProcessing(Order order);
}
