package org.assignment.ui;

import org.assignment.entities.CartItems;
import org.assignment.entities.Customer;

import org.assignment.enums.OrderStatus;
import org.assignment.enums.ResponseStatus;
import org.assignment.enums.Roles;

import org.assignment.exceptions.UnauthorizedOperationException;

import org.assignment.repository.interfaces.OrderRepository;
import org.assignment.services.CustomerService;

import org.assignment.services.OrderService;
import org.assignment.util.ColorCodes;
import org.assignment.util.Response;
import org.assignment.wrappers.ProductCountWrappers;

import java.awt.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CustomerUI extends UI {
    private final Scanner sc = new Scanner(System.in);
    private final CustomerService service;
    private final OrderService orderService;

    public CustomerUI(CustomerService service, OrderService orderService) {
        this.service = service;
        this.orderService = orderService;
    }

    public void initCustomerServices(Customer customer) {
        System.out.println(ColorCodes.GREEN + "******BROWSE*******" + ColorCodes.RESET);
        Scanner sc = new Scanner(System.in);
        System.out.println(ColorCodes.GREEN + "********WELCOME-CUSTOMER*******" + ColorCodes.RESET);
        String operation = "";
        List<String> options = new ArrayList<>();
        options.add("PRESS 1 FOR TO START ORDERING OR GO TO CART");
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
            System.out.println("PRESS 0 TO EDIT CART");
            System.out.println("PRESS -1 TO REMOVE ITEMS FROM CART");
            System.out.println("PRESS 1 TO CHECKOUT");
            System.out.println("PRESS 2 TO EXIT CART");
            System.out.println(ColorCodes.BLUE + "Cart : " + customer.getCart() + ColorCodes.RESET);
            System.out.print("product name : ");
            name = sc.nextLine().toUpperCase();
            boolean condition = name.equalsIgnoreCase("0")
                    || name.equalsIgnoreCase("-1")
                    || name.equalsIgnoreCase("1");
            if (condition) {
                break;
            }
            int quantity = 0;
            while(quantity <= 0)
            {
                disclaimerForQuantity();
                System.out.println("Please provide the quantity for the product");
                try {
                    quantity = sc.nextInt();
                }catch (InputMismatchException e){
                    System.out.println(ColorCodes.RED + "Inputs must be in Integer only" + ColorCodes.RESET);
                    sc.next();
                }
            }
            Response response = service.intiateCart(customer, name, quantity);
           printResponseOutput(response);
            sc.nextLine();
        }
        if (name.equalsIgnoreCase("0")) {
            editCart(customer);
        } else if (name.equalsIgnoreCase("-1")) {
            removeFromCart(customer);
        } else if (name.equalsIgnoreCase("1")) {
            chechkOut(customer);
        }

    }

    private void chechkOut(Customer customer) {
        String message = "";
        boolean error = false;
        int size = customer.getCart().size();
        for (CartItems items : customer.getCart()) {
            Response response = service.orderFromCart(customer, items.getProduct().getName(), items.getQuantity(), false);
            error = response.getStatus() == ResponseStatus.ERROR;
            if (error) {
                message = response.getError();
                break;
            }
        }
        message = error ? message : size + " items ordered from cart";
        System.out.println(ColorCodes.GREEN + message + ColorCodes.RESET);

    }

    private void removeFromCart(Customer customer) {
        String name = "";
        while (!name.equalsIgnoreCase("-1")) {
            System.out.println("Enter product name to be removed from cart ( '-1' to go back)");
            name = sc.nextLine();
            if (name.equalsIgnoreCase("-1")) {
                break;
            }
            Response response = service.removeFromCart(customer, name);
            printResponseOutput(response);
        }
    }

    private void getAllOrders(Customer customer) {
        Response response = service.getAllOrders(customer);
       printResponseOutput(response);
    }

    private void cancelOrder(Customer customer) {
        Response response = orderService.fetchOrdersByCustomerAndOrderStatus(customer, OrderStatus.ORDERED);
        if (response.getStatus() == ResponseStatus.ERROR) {
            System.out.println(ColorCodes.RED + response.getError() + ColorCodes.RESET);
            return;
        }
        System.out.println("Your orders : ");
        System.out.print(ColorCodes.BLUE + response.getData() + ColorCodes.RESET);
        System.out.print("Please provide the product name whose order you want to cancel : ");
        String productName = sc.nextLine().toUpperCase();
        response = service.cancelOrder(1, customer, productName, false);
        if (response.getStatus() == ResponseStatus.SUCCESSFUL) {
            System.out.println(ColorCodes.GREEN + "Order cancelled" + ColorCodes.RESET);
        } else if (response.getStatus() == ResponseStatus.PROCESSING) {
            proceedToMultipleCancellation(customer, (ProductCountWrappers) response.getData());
        } else {
            System.out.println(ColorCodes.RED + response.getError() + ColorCodes.RESET);
        }
    }

    private void proceedToMultipleCancellation(Customer customer, ProductCountWrappers wrappers) {
        System.out.println(ColorCodes.YELLOW + wrappers + ColorCodes.RESET);
        System.out.println("Please provide the quantity of the product : ");
        int count = sc.nextInt();
        while (count > wrappers.getQuantity() || count <= 0) {
            System.err.println("quantity must not be greater or lesser than quantity");
            count = sc.nextInt();
        }
        Response response = service.cancelOrder(count, customer, wrappers.getProductName(), true);
       printResponseOutput(response);
    }

    private void orderFromCart(Customer customer) {
        System.out.print(ColorCodes.BLUE + customer.getCart() + ColorCodes.RESET);
        System.out.println("Please provide the product which needs to be ordered from cart : ");
        String productName = sc.nextLine();
        boolean exists = (boolean) service.hasProduct(customer, productName).getData();
        while (!exists) {
            System.out.println(ColorCodes.RED + "Incorrect product name" + ColorCodes.RESET + "\nplease provide the correct product name :");
            System.out.println("Type '-1' to exit");
            productName = sc.nextLine();
            if (productName.trim().equalsIgnoreCase("-1")) {
                break;
            }
            exists = (boolean) service.hasProduct(customer, productName).getData();
        }
        if (!productName.equalsIgnoreCase("-1")) {
            int quantity = 0;
            while(quantity <= 0)
            {
                disclaimerForQuantity();
                System.out.println("Please provide the quantity for the product");
                quantity = sc.nextInt();

            }
            Response response = service.orderFromCart(customer, productName,quantity, true);
            printResponseOutput(response);
        } else {
            System.out.println(ColorCodes.BLUE + "Going back" + ColorCodes.RESET);
        }
    }
    private void editCart(Customer customer)
    {
        System.out.println( ColorCodes.BLUE + "Your cart : " + ColorCodes.RESET);
        System.out.println(ColorCodes.YELLOW + customer.getCart() + ColorCodes.RESET);
        System.out.println(ColorCodes.GREEN + "Please provide the product name whose quantity you want to update" + ColorCodes.RESET);
        String product = sc.nextLine();
        Response checkResponse = service.hasProduct(customer, product);
        boolean check = (boolean) checkResponse.getData();
        while ( ! check){
            System.out.println(ColorCodes.RED + "Incorrect product name please enter correct product name " + ColorCodes.RESET);
            product = sc.nextLine();
            checkResponse = service.hasProduct(customer, product);
             check = (boolean) checkResponse.getData();
        }
        System.out.println(ColorCodes.GREEN + "Press 1 to increase quantity of product\nPress 2 to decrease quantity of product" + ColorCodes.RESET);
        String choice = sc.nextLine();
        while(! choice.equalsIgnoreCase("1") && ! choice.equalsIgnoreCase("2"))
        {
            System.out.println(ColorCodes.RED + "You have entered wrong choice please a correct option" + ColorCodes.RESET);
            choice = sc.nextLine();
        }
        if(choice.equalsIgnoreCase("1")){
            incrementQuantity(customer, product);
        } else if (choice.equalsIgnoreCase("2")) {
            decrementQuantity(customer, product);
        }
    }
    private void disclaimerForQuantity()
    {
        System.out.println(ColorCodes.RED + "Disclaimer:\n" +
                "Quantity must be greater than 0" + ColorCodes.RESET);
    }
    private void incrementQuantity(Customer customer, String productName)
    {
       int quantity = getValidInputsForQuantityUpdate();
        Response response = service.incrementQuantity(customer, productName, quantity);
        printResponseOutput(response);
    }
    private void decrementQuantity(Customer customer, String productName)
    {
        int quantity = getValidInputsForQuantityUpdate();
        Response response = service.decrementQuantity(customer, productName, quantity);
        printResponseOutput(response);
    }
    private int getValidInputsForQuantityUpdate()
    {
        System.out.print(ColorCodes.GREEN + "Quantity : " + ColorCodes.RESET);
        int quantity = sc.nextInt();
        while (quantity <= 0)
        {
            System.out.println(ColorCodes.RED + "Quantity must be greater than zero" + ColorCodes.RESET);
            System.out.println(ColorCodes.GREEN + "Quantity : " + ColorCodes.RESET);
            quantity = sc.nextInt();

        }
        sc.nextLine();
        return quantity;
    }
    private void printResponseOutput(Response response)
    {
        if(response.getStatus() == ResponseStatus.SUCCESSFUL){
            System.out.println(ColorCodes.RED + response.getData() + ColorCodes.RESET);
        } else if (response.getStatus() == ResponseStatus.ERROR) {
            System.out.println(ColorCodes.RED + response.getError() + ColorCodes.RESET);
        }
    }
}