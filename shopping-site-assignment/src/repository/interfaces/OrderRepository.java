package repository.interfaces;

import entities.Order;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
      List<Order> getAllOrders();
    Optional<Order> fetchOrderById(Long id);
    boolean cancelOrder(Order order);
    void addOrder(Order order);
    List<Order> fetchOrderByProductName(String name);
    public List<Order> getOrderByCustomerId(Long id);
    List<Order> getAllDeliveredOrders();

}
