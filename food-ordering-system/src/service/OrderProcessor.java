package service;

import entities.Order;
import enums.OrderStatus;

public class OrderProcessor implements Runnable {
    private final Order order;

    public OrderProcessor(Order order) {
        this.order = order;
    }

    @Override
    public void run() {
        try {
            // System.out.println("Order ID: " + order.getId() + " is now being PROCESSED.");
            Thread.sleep(5000);
            order.setOrderStatus(OrderStatus.PROCESSING);

            // System.out.println("Order ID: " + order.getId() + " is now COMPLETED.");
            Thread.sleep(15000);
            order.setOrderStatus(OrderStatus.COMPLETED);

        } catch (InterruptedException e) {
            System.out.println("Order processing was interrupted for Order ID: " + order.getId());
            order.setOrderStatus(OrderStatus.CANCELED);
        }
    }
}