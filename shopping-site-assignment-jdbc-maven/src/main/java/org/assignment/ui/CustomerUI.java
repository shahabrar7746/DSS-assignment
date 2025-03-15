package org.assignment.ui;

import org.assignment.entities.Customer;

import org.assignment.enums.ResponseStatus;
import org.assignment.enums.Roles;

import org.assignment.exceptions.UnauthorizedOperationException;

import org.assignment.services.CustomerService;

import org.assignment.util.ColorCodes;
import org.assignment.util.Response;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerUI  extends  UI {
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
        Response response = null;
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
            switch (operation){
                case "1" -> {
                    response = startOrder(customer);
                }case "2"->{
                    response = viewOrders(customer);
                }case "3"->{
                    response = cancelOrder(customer);
                }
                case "4"->{
                    response = orderFromCart(customer);
                }
                case "back", "BACK"->{
                response = new Response("");
                }default -> System.out.println("Invalid operation");
            }
            if(response.getStatus() == ResponseStatus.SUCCESSFUL){
                System.out.println(response.getData());
            }else {
                System.err.println(response.getError());
            }
        }

        super.displayOptions(List.of("Program ended"));
    }

private Response startOrder(Customer customer){
    List<String> options = new ArrayList<>();
    options.add("PRESS 1 TO PROCEED ORDER");
    options.add("PRESS 2 TO GO TO CART");
    options.add("PRESS -1 TO EXIT");

    boolean exit = false;
    int choice = sc.nextInt();
    Response response = null;
    while( ! exit){
        super.displayOptions(options);
        if(choice == 1){
            //proceed to order.
        } else if (choice == 2) {
            //go to cart.
        } else if (choice == -1) {
            exit = true;
        }else {
            System.out.println("In valid choice");
        }
    }

    return response;
}
private  Response viewOrders(Customer customer){
        Response response = null;//fetch orders and put in inside response.
    //and display them here.
    String choice = "";
    while ( ! choice.equalsIgnoreCase("n")) {
        super.displayOptions(List.of("Want to cancel order, y ? n "));
        choice = sc.nextLine().toUpperCase();
        if (choice.equalsIgnoreCase("y")) {
            //proceed to cancell Order.
            response = cancelOrder(customer);
        }else if (! choice.equalsIgnoreCase("n")){
            System.out.println("Invalid input");
        }
    }
    return response == null ? new Response("") : response;
}
private  Response cancelOrder(Customer customer){
        //fetch all orders and request for order name or id.
    Response response = null;
    String orderName = "";
    while (! orderName.equalsIgnoreCase("0")) {
        super.displayOptions(List.of("Provide order name that need to be cancelled or else press 0 to exit"));
        orderName = sc.nextLine().toUpperCase();
        if(orderName.equalsIgnoreCase("0")){
            break;
        }
       response = //
        if(response.getStatus() == ResponseStatus.SUCCESSFUL){
            break;
        }

    }
    //give order name and customer obj to service for order cancellation.
    return response;

}
private Response orderFromCart(Customer customer){
        Response response = null;
        //fetch and display cart items.
    super.displayOptions(List.of("Provide product name from cart to be ordered or else press 0 to exit cart"));
    String pName = sc.nextLine();
    if(pName.equalsIgnoreCase("0")){
        return new Response("Exiting cart");
    }

}
}
