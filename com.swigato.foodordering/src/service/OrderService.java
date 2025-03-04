package service;

import dao.OrderDao;
import entities.Order;
import enums.OrderStatus;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OrderService {

    private final OrderDao orderDao;

    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void placeOrder(Order order) {
        orderDao.addOrder(order);
        simulateOrderProcessing(order);
    }

    public Order getOrder(int orderId) {
        return orderDao.getOrderById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderDao.getAllOrders();
    }

    private void simulateOrderProcessing(Order order) {

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000);
                order.setOrderStatus(OrderStatus.PROCESSING);
                orderDao.updateOrder(order);

                Thread.sleep(15000);
                order.setOrderStatus(OrderStatus.COMPLETED);
                orderDao.updateOrder(order);

            } catch (InterruptedException e) {
                System.out.println("Order processing was interrupted for Order ID: " + order.getId());
                order.setOrderStatus(OrderStatus.CANCELED);
            }
        });
        thread.start();
    }
}