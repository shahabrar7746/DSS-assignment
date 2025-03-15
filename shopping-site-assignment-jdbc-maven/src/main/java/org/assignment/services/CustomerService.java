package org.assignment.services;

import org.assignment.entities.Customer;
import org.assignment.serviceimlementation.CustomerServiceImplementation;

import java.sql.SQLException;

public interface CustomerService {
       /**
        * Acts as UI for user.
        * @param customer - indicates for which customer service is being called.
        * @throws  Exception for any negative scenario.
        */
       void browse(final Customer customer, String operation) throws SQLException;
       static CustomerService getInstance(){
              return CustomerServiceImplementation.getInstance();
       }
}
