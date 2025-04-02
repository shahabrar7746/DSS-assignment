package org.assignment.repository.interfaces;

import org.assignment.entities.Customer;
import org.assignment.entities.Order;
import org.assignment.entities.Product;
import org.assignment.enums.OrderStatus;
import org.assignment.exceptions.CustomerNotFoundException;
import org.assignment.exceptions.NoProductFoundException;
import org.assignment.exceptions.OrderNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
      List<Order> getAllOrders() throws SQLException, CustomerNotFoundException, NoProductFoundException;
    Optional<Order> fetchOrderById(Long id) throws Exception;
    void cancelOrder(Order order) throws SQLException;
    Order addOrder(Order order) throws SQLException;
    List<Order> fetchOrderByProductAndCustomer(Product product, Customer customer) throws SQLException, CustomerNotFoundException, NoProductFoundException;
     List<Order> getOrderByCustomer(Customer customer) throws SQLException, CustomerNotFoundException, NoProductFoundException, OrderNotFoundException;
    List<Order> getAllDeliveredOrders() throws SQLException, CustomerNotFoundException, OrderNotFoundException, NoProductFoundException;
Order updateOrder(Order order);
List<Order> getOrdersByStatusAndCustomer(Customer customer, OrderStatus status);

}
