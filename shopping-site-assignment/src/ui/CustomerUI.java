package ui;

import entities.Customer;

import enums.Roles;

import exceptions.UnauthorizedOperationException;

import services.CustomerService;

import serviceimlementation.CustomerServiceImplementation;

import util.ColorCodes;

public class CustomerUI  implements UserInterface {

    private final CustomerService service;
    public CustomerUI() {
        this.service = new CustomerServiceImplementation();
    }

    @Override
    public void init(Customer customer) {
        if (customer.getRole() != Roles.CUSTOMER) {
            throw new UnauthorizedOperationException("Operation not supported");
        }
        System.out.println(ColorCodes.GREEN + "********WELCOME-CUSTOMER*******" + ColorCodes.RESET);
        service.browse(customer);
        System.out.println("Program ended");
    }
}
