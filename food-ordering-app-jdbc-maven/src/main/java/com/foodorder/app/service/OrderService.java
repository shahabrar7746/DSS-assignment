package com.foodorder.app.service;

import com.foodorder.app.entities.Order;
import com.foodorder.app.utility.Response;

import java.util.List;

public interface OrderService {
    Response getOrder(int orderId);

    Response getAllOrders();

    void placeOrder(Order order);

    void simulateOrderProcessing(Order order);
}
