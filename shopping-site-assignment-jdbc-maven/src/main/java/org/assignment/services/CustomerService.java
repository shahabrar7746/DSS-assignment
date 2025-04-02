package org.assignment.services;

import org.assignment.entities.Customer;
import org.assignment.serviceimlementation.CustomerServiceImplementation;
import org.assignment.util.Response;

import java.sql.SQLException;
import java.util.Optional;

public interface CustomerService {


    Optional<Customer> findByEmail(String email);

    Response intiateCart(Customer customer, String name, int quantity);

    Response removeFromCart(final Customer customer, String name);

    Response getAllOrders(Customer customer);

    Response cancelOrder(int count, Customer customer, String productName, boolean multiple);

    Response hasProduct(Customer customer, String productName);

    Response orderFromCart(Customer customer, String name, int quantity, boolean edited);

    Response incrementQuantity(Customer customer, String productName, int newQuantity);

    Response decrementQuantity(Customer customer, String productName, int newQuantity);
}
