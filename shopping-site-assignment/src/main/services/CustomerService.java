package main.services;

import main.entities.Customer;
import main.serviceimlementation.CustomerServiceImplementation;

import java.sql.SQLException;

public interface CustomerService {
       /**
        * Acts as UI for user.
        * @param customer - indicates for which customer service is being called.
        * @throws  Exception for any negative scenario.
        */
       void browse(final Customer customer) throws SQLException;
       static CustomerService getInstance(){
              return CustomerServiceImplementation.getInstance();
       }
}
