package dao;

import entities.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    void addOrder(Order order);

    Optional<Order> getOrderById(int id);

    List<Order> getAllOrders();

    void updateOrder(Order order);

    void deleteOrder(Order order);
}