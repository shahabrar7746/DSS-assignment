package ui;

import entities.Customer;

public interface UserInterface {
    /**
     * Displays options based on role authority, i.e. admin, super admin or customer.
     * @param  user  used to verify authority of object.
     */
    void init(Customer user);
}
