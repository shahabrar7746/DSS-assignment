package org.assignment.services;

import org.assignment.entities.CartItems;
import org.assignment.entities.Customer;
import org.assignment.serviceimlementation.CustomerServiceImplementation;
import org.assignment.util.Response;

import java.sql.SQLException;
import java.util.Optional;

public interface CustomerService {

    Response getCustomerById(Long id);

    Optional<Customer> findByEmail(String email);


    Response getAllCustomer();

    Response deleteCustomer(Long cid);

    Response fetchAllAdmins();

    boolean authenticateSuperAdmin(String password);

    Response customerExists(Long id);

    Response isAdmin(Long id);
}
