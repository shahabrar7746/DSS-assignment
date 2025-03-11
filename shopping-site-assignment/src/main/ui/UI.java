package main.ui;


import main.entities.Customer;
import main.exceptions.UnauthorizedOperationException;

import java.sql.SQLException;
import java.util.List;

public abstract class UI {

    public void initAdminServices(Customer admin){
    }
    public void initCustomerServices(Customer customer) throws UnauthorizedOperationException, SQLException {}
    public void displayOptions(List<String> options){
        options.forEach(System.out::println);
    }
    //customerUi
}
