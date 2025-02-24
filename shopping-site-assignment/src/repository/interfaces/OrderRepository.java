package repository.interfaces;

import entities.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
      List<Order> getAllOrders();
    Optional<Order> fetchOrderById(Long id);
    boolean cancelOrder(Order order);
    void addOrder(Order order);
    Optional<List<Order>> fetchOrderByProductName(String name);

}
