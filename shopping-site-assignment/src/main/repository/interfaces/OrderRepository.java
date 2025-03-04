package main.repository.interfaces;

import main.entities.Order;
import main.exceptions.CustomerNotFoundException;
import main.exceptions.NoProductFoundException;
import main.exceptions.OrderNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
      List<Order> getAllOrders() throws Exception;
    Optional<Order> fetchOrderById(Long id) throws Exception;
    boolean cancelOrder(Order order) throws SQLException;
    void addOrder(Order order) throws Exception;
    List<Order> fetchOrderByProductName(String name) throws SQLException, CustomerNotFoundException, NoProductFoundException;
    public List<Order> getOrderByCustomerId(Long id) throws SQLException, CustomerNotFoundException, NoProductFoundException;
    List<Order> getAllDeliveredOrders() throws SQLException, CustomerNotFoundException, OrderNotFoundException, NoProductFoundException;

}
