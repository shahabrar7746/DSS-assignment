package main.ui;

import main.entities.Customer;

import main.enums.Roles;

import main.exceptions.UnauthorizedOperationException;

import main.services.CustomerService;

import main.serviceimlementation.CustomerServiceImplementation;

import main.util.ColorCodes;

import java.sql.SQLException;

public class CustomerUI  extends  UI {

    private final CustomerService service;
    public CustomerUI() {
        this.service = CustomerService.getInstance();
    }


    public void initCustomerServices(Customer customer) throws UnauthorizedOperationException, SQLException {
        if (customer.getRole() != Roles.CUSTOMER) {
            throw new UnauthorizedOperationException("Operation not supported");
        }
        System.out.println(ColorCodes.GREEN + "********WELCOME-CUSTOMER*******" + ColorCodes.RESET);
        service.browse(customer);
        System.out.println("Program ended");
    }
}
