package main.ui;


import main.entities.Customer;
import main.exceptions.UnauthorizedOperationException;

import java.sql.SQLException;

public abstract class UI {

    public void initAdminServices(Customer admin){
    }
    public void initCustomerServices(Customer customer) throws UnauthorizedOperationException, SQLException {}
    //customerUi
}
