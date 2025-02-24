package services;

import entities.Customer;

public interface CustomerService {
       /**
        * Acts as UI for user.
        * @param customer - indicates for which customer service is being called.
        * @throws  Exception for any negative scenario.
        */
       void browse(final Customer customer);
}
