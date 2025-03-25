package org.assignment.services;

import org.assignment.entities.Customer;
import org.assignment.serviceimlementation.CustomerServiceImplementation;

import java.sql.SQLException;
import java.util.Optional;

public interface CustomerService {

       void browse(final Customer customer, String operation) throws SQLException;
       static CustomerService getInstance(){
              return CustomerServiceImplementation.getInstance();
       }
       Optional<Customer> findByEmail(String email);

}
