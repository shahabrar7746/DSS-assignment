package service;

import entities.Order;

import java.util.List;

public interface OrderService {
    Order getOrder(int orderId);

    List<Order> getAllOrders();

    void placeOrder(Order order);

    void simulateOrderProcessing(Order order);
}
