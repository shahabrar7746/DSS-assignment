package org.assignment.ui;

import org.assignment.entities.CartItems;
import org.assignment.entities.Customer;

import org.assignment.enums.ResponseStatus;
import org.assignment.enums.Roles;

import org.assignment.exceptions.NoProductFoundException;
import org.assignment.exceptions.UnauthorizedOperationException;

import org.assignment.repository.interfaces.ProductRepository;
import org.assignment.repositoryhibernateimpl.ProductRepoHibernateImpl;
import org.assignment.services.CustomerService;

import org.assignment.util.ColorCodes;
import org.assignment.util.LogUtil;
import org.assignment.util.Response;
import org.assignment.wrappers.ProductWrappers;
import org.hibernate.HibernateException;

import java.awt.*;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerUI extends UI {
    private final Scanner sc = new Scanner(System.in);
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
        while (!operation.equalsIgnoreCase("back")) {
            super.displayOptions(options);
            operation = sc.nextLine();
            switch (operation) {
               case "1" -> startOrdering(customer);
                case "2" -> getAllOrders(customer);
                case "3" -> cancelOrder(customer);
                case "4" -> orderFromCart(customer);
                case "back", "BACK" -> System.out.println("Going back");
                default -> System.out.println("Invalid input");
            }
        }

    }

    private void startOrdering(Customer customer) {
        String name = "";
        while (!name.equalsIgnoreCase("0") || !name.equalsIgnoreCase("-1")) {
            System.out.println("PRESS 0 TO REMOVE PRODUCT FROM CART");
            System.out.println("PRESS -1 TO EXIT CART");
            System.out.println(ColorCodes.BLUE + "Cart : " + customer.getCart() + ColorCodes.RESET);
            System.out.print("product name : ");
             name = sc.nextLine().toUpperCase();
            boolean condition = name.equalsIgnoreCase("0") || name.equalsIgnoreCase("-1");
            if( condition){
                break;
            }
            Response response = service.intiateCart(customer, name);

            if (response.getStatus() == ResponseStatus.ERROR) {
                System.out.println(ColorCodes.RED + response.getError() + ColorCodes.RESET);
            } else {
                System.out.println(ColorCodes.GREEN + response.getData() + ColorCodes.RESET);
            }
        }
        if (name.equalsIgnoreCase("0")) {
            removeFromCart(customer);
        }

    }

    private void removeFromCart(Customer customer) {
        String name = "";
        while (!name.equalsIgnoreCase("-1")) {
            System.out.println("Enter product name to be removed from cart ( '-1' to go back)");
            name = sc.nextLine();
            if(name.equalsIgnoreCase("-1")){
                break;
            }
         Response response =   service.removeFromCart(customer, name);
            if (response.getStatus() == ResponseStatus.ERROR) {
                System.out.println(ColorCodes.RED + response.getError() + ColorCodes.RESET);
            } else {
                System.out.println(ColorCodes.BLUE + response.getData() + ColorCodes.RESET);
            }
        }
    }

    private void getAllOrders(Customer customer) {
        Response response = service.getAllOrders(customer);
        if (response.getStatus() == ResponseStatus.ERROR) {
            System.out.println(ColorCodes.RED + response.getError() + ColorCodes.RESET);
        } else {
            System.out.println(ColorCodes.BLUE + response.getData() + ColorCodes.RESET);
        }
    }

    private void cancelOrder(Customer customer) {
        Response response = service.getAllOrders(customer);
        if (response.getStatus() == ResponseStatus.ERROR) {
            System.out.println(ColorCodes.RED + response.getError() + ColorCodes.RESET);
            return;
        }
        System.out.println("Your orders : ");
        System.out.print(ColorCodes.BLUE + response.getData() + ColorCodes.RESET);
        System.out.print("Please provide the product name whose order you want to cancel : ");
        String productName = sc.nextLine().toUpperCase();
        response = service.cancelOrder(0, customer, productName);
        if (response.getStatus() == ResponseStatus.SUCCESSFUL) {
            System.out.println(ColorCodes.GREEN + "Order cancelled" + ColorCodes.RESET);
        } else if (response.getStatus() == ResponseStatus.PROCESSING) {
            proceedToMultipleCancellation(customer, (ProductWrappers) response.getData());
        } else {
            System.out.println(ColorCodes.RED + response.getError() + ColorCodes.RESET);
        }
    }

    private void proceedToMultipleCancellation(Customer customer, ProductWrappers wrappers) {
        System.out.println(ColorCodes.YELLOW + wrappers + ColorCodes.RESET);
        System.out.println("Please provide the quantity of the product : ");
        int count = sc.nextInt();
        while (count > wrappers.getQuantity() || count <= 0) {
            System.err.println("quantity must not be greater or lesser than quantity");
            count = sc.nextInt();
        }
        Response response = service.cancelOrder(count, customer, wrappers.getProductName());
        if (response.getStatus() == ResponseStatus.ERROR) {
            System.out.println(ColorCodes.RED + response.getError() + ColorCodes.RESET);
        } else {
            System.out.println(ColorCodes.BLUE + response.getData() + ColorCodes.RESET);
        }
    }
    private void orderFromCart(Customer customer)
    {
        System.out.print(ColorCodes.BLUE + customer.getCart() + ColorCodes.RESET);
        System.out.println("Please provide the product which needs to be ordered from cart : ");
         String productName = sc.nextLine();
         boolean exists = (boolean) service.hasProduct(customer, productName).getData();
         while (! exists){
             System.out.println(ColorCodes.RED + "Incorrect product name" + ColorCodes.RESET + "\nplease provide the correct product name :");
             System.out.println("Type '-1' to exit");
             productName = sc.nextLine();
             if(productName.equalsIgnoreCase("-1")){
                 break;
             }
             exists = (boolean) service.hasProduct(customer, productName).getData();
         }
         Response response = service.orderFromCart(customer, productName);
        if (response.getStatus() == ResponseStatus.ERROR) {
            System.out.println(ColorCodes.RED + response.getError() + ColorCodes.RESET);
        } else {
            System.out.println(ColorCodes.BLUE + response.getData() + ColorCodes.RESET);
        }
    }
}