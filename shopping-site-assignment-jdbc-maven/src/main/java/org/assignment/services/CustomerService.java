package org.assignment.services;

import org.assignment.entities.Customer;
import org.assignment.serviceimlementation.CustomerServiceImplementation;
import org.assignment.util.Response;

import java.sql.SQLException;
import java.util.Optional;

public interface CustomerService {

    static CustomerService getInstance() {
        return CustomerServiceImplementation.getInstance();
    }

    Optional<Customer> findByEmail(String email);

    Response intiateCart(Customer customer, String name);

    Response removeFromCart(final Customer customer, String name);

    Response getAllOrders(Customer customer);

    Response cancelOrder(int count, Customer customer, String productName);

    Response hasProduct(Customer customer, String productName);

    Response orderFromCart(Customer customer, String name);
}
