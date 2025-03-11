package main.ui;

import main.entities.Customer;

import main.enums.Roles;

import main.exceptions.UnauthorizedOperationException;

import main.services.CustomerService;

import main.serviceimlementation.CustomerServiceImplementation;

import main.util.ColorCodes;

import javax.swing.plaf.SeparatorUI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerUI  extends  UI {

    private final CustomerService service;
    public CustomerUI() {
        this.service = CustomerService.getInstance();
    }


    public void initCustomerServices(Customer customer) throws UnauthorizedOperationException, SQLException {
        if (customer.getRole() != Roles.CUSTOMER) {
            throw new UnauthorizedOperationException("Operation not supported");
        }
        System.out.println(ColorCodes.GREEN + "******BROWSE*******" + ColorCodes.RESET);
        Scanner sc = new Scanner(System.in);
        System.out.println(ColorCodes.GREEN + "********WELCOME-CUSTOMER*******" + ColorCodes.RESET);
        String operation = "";
        List<String> options = new ArrayList<>();
        options.add("PRESS 1 FOR TO START ORDERING");
        options.add("PRESS 2 TO VIEW ORDERS");
        options.add("PRESS 3 TO CANCEL ORDER");
        options.add("PRESS 4 TO ORDER ITEMS FROM CART");
        options.add("TYPE 'BACK' TO GO BACK");
        options.add("Operation : ");
        while (!operation.equalsIgnoreCase("back")){
            super.displayOptions(options);
            operation = sc.nextLine();
            service.browse(customer, operation);
        }

        super.displayOptions(List.of("Program ended"));
    }

}
