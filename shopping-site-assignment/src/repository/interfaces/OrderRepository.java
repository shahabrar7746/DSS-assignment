package repository.interfaces;

import entities.Order;
import exceptions.CustomerNotFoundException;
import exceptions.OrderNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
      List<Order> getAllOrders() throws Exception;
    Optional<Order> fetchOrderById(Long id) throws Exception;
    boolean cancelOrder(Order order);
    void addOrder(Order order) throws Exception;
    List<Order> fetchOrderByProductName(String name) throws Exception;
    public List<Order> getOrderByCustomerId(Long id) throws SQLException, CustomerNotFoundException;
    List<Order> getAllDeliveredOrders() throws SQLException, CustomerNotFoundException, OrderNotFoundException;

}
