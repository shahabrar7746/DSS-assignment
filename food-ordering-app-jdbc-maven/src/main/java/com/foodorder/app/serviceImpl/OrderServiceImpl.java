package com.foodorder.app.serviceImpl;


import com.foodorder.app.dao.OrderDao;
import com.foodorder.app.entities.Order;
import com.foodorder.app.enums.OrderStatus;
import com.foodorder.app.enums.ResponseStatus;
import com.foodorder.app.service.OrderService;
import com.foodorder.app.utility.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    Response response;
    Logger logger = LogManager.getLogger();

    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void placeOrder(Order order) {
        orderDao.addOrder(order);
        simulateOrderProcessing(order);
    }

    public Response getOrder(int orderId) {
        try {
            Optional<Order> orderById = orderDao.getOrderById(orderId);

            if (orderById.isPresent()) {
                Order order = orderById.get();
                response = new Response(order, ResponseStatus.SUCCESS, "Order details fetched successfully.");
            } else {
                response = new Response(ResponseStatus.FAILURE, "Invalid order ID or order not found.");
            }
        } catch (Exception e) {
            logger.error("Error in get order method from order service impl:" + e );
            response = new Response(ResponseStatus.FAILURE, "An unexpected error occurred while retrieving the order.");
        }
        return response;
    }

    public Response getAllOrders() {
        List<Order> allOrders = orderDao.getAllOrders();
        if (allOrders == null || allOrders.isEmpty()) {
            response = new Response(ResponseStatus.FAILURE, "You have no past orders");
        } else {
            response = new Response(allOrders, ResponseStatus.SUCCESS, "Successfully fetched orders");
        }
        return response;
    }

    public void simulateOrderProcessing(Order order) {

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000);
                order.setOrderStatus(OrderStatus.PROCESSING);
                orderDao.updateOrder(order);

                Thread.sleep(15000);
                order.setOrderStatus(OrderStatus.COMPLETED);
                orderDao.updateOrder(order);

            } catch (InterruptedException e) {
                logger.error("Error while simulating the order process from order service impl" + e);
                System.out.println("Order processing was interrupted for Order ID: " + order.getId());
                order.setOrderStatus(OrderStatus.CANCELED);
            }
        });
        thread.start();
    }
}
