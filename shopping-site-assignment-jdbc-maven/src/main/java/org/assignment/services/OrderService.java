package org.assignment.services;

import org.assignment.entities.User;
import org.assignment.enums.OrderStatus;
import org.assignment.util.Response;

public interface OrderService {

    Response fetchOrdersByCustomerAndOrderStatus(User user, OrderStatus status);

    Response cancelOrder(int count, User user, String productName, boolean multiple);

    Response getAllOrdersByCustomer(User user);

    Response orderCart(User user);



}
