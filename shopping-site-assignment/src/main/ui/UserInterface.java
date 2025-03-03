package main.ui;

import main.entities.Customer;
import main.exceptions.UnauthorizedOperationException;

import java.sql.SQLException;

public interface UserInterface {
    /**
     * Displays options based on role authority, i.e. admin, super admin or customer.
     * @param  user  used to verify authority of object.
     */
    void init(Customer user) throws UnauthorizedOperationException, SQLException;
}
