package org.assignment.services;

import org.assignment.entities.Customer;
import org.assignment.entities.Order;
import org.assignment.enums.OrderStatus;
import org.assignment.util.Response;

import java.util.List;

public interface OrderService {

    Response fetchOrdersByCustomerAndOrderStatus(Customer customer, OrderStatus status);

    Response getAllOrders(Customer customer);

    Response cancelOrder(int count, Customer customer, String productName, boolean multiple);

    Response getAllDeliveredOrders();
}
