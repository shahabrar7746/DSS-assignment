package service;

import entities.Order;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderService {
    private static Map<Integer, Order> orders = new ConcurrentHashMap<>();

//    static {
//        orders = new ConcurrentHashMap<>();
//    }

    public static Order getOrder(int orderId) {
        return orders.get(orderId);
    }

    public static void placeOrder(Order order) {
        orders.put(order.getId(), order);
        System.out.println("Order ID: " + order.getId() + " has been PLACED.");

        OrderProcessor orderProcessor = new OrderProcessor(order);
        Thread thread = new Thread(() -> {
        });
        thread.start();
    }

    public static Map<Integer, Order> getAllOrders() {
        return orders;
    }
}
