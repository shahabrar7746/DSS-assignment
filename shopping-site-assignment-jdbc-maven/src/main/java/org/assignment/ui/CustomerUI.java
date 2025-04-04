package org.assignment.ui;

import lombok.AllArgsConstructor;
import org.assignment.entities.CartItems;
import org.assignment.entities.Customer;

import org.assignment.enums.OrderStatus;
import org.assignment.enums.ResponseStatus;

import org.assignment.services.CartService;

import org.assignment.services.OrderService;
import org.assignment.services.ProductService;
import org.assignment.util.ColorCodes;
import org.assignment.util.Response;
import org.assignment.wrappers.ProductCountWrappers;


import java.util.*;
@AllArgsConstructor
public class CustomerUI extends UI {
    private final Scanner sc = new Scanner(System.in);
    private final OrderService orderService;
private final ProductService productService;
private final CartService cartService;
    @Override
    public void initCustomerServices(Customer customer) {
        System.out.println(ColorCodes.GREEN + "********WELCOME-CUSTOMER*******" + ColorCodes.RESET);
        System.out.println(ColorCodes.GREEN + "******BROWSE*******" + ColorCodes.RESET);

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
        while (!name.equalsIgnoreCase("2")) {
            Response wrapperResponse = productService.getAllProduct();
            if(wrapperResponse.getStatus() == ResponseStatus.SUCCESSFUL && wrapperResponse.getData() instanceof  List<?> wrappers)
            {
                System.out.println(ColorCodes.GREEN + wrappers + ColorCodes.RESET);
            }else{
                System.out.println(ColorCodes.GREEN + wrapperResponse.getError() + ColorCodes.RESET);
            }

            System.out.println("PRESS 0 TO EDIT CART");
            System.out.println("PRESS -1 TO REMOVE ITEMS FROM CART");
            System.out.println("PRESS 1 TO CHECKOUT");
            System.out.println("PRESS 2 TO EXIT CART");
            System.out.println(ColorCodes.BLUE + "Cart : " + customer.getCart() + ColorCodes.RESET);
            printResponseOutput(cartService.getTotalBillFromCart(customer));
            System.out.print("product name : ");
            name = sc.nextLine().toUpperCase();


            if (name.equalsIgnoreCase("0")) {
                editCart(customer);
            } else if (name.equalsIgnoreCase("-1")) {
                removeFromCart(customer);
            } else if (name.equalsIgnoreCase("1")) {
                chechkOut(customer);
            } else if (name.equalsIgnoreCase("2")) {
                break;
            } else {
                int quantity = 0;
                while (quantity <= 0) {
                    disclaimerForQuantity();
                    System.out.println("Please provide the quantity for the product (press '-1' to go to back)");
                    try {
                        quantity = sc.nextInt();
                        if (quantity == -1) {
                            return;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println(ColorCodes.RED + "Inputs must be in Integer only" + ColorCodes.RESET);
                        sc.next();
                    }
                }

                Response response = cartService.intiateCart(customer, name, quantity);
                printResponseOutput(response);
                sc.nextLine();
            }

        }
    }

    private void chechkOut(Customer customer) {
        if (customer.getCart().isEmpty()) {
            System.out.println(ColorCodes.RED + "Cart is empty " + ColorCodes.RESET);
            return;
        }
        String message = "";
        boolean error = false;
        int size = customer.getCart().size();
        for (CartItems items : customer.getCart()) {
            Response response = cartService.orderFromCart(customer, items.getProduct().getName(), items.getQuantity(), false);
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
        if (customer.getCart().isEmpty()) {
            System.out.println(ColorCodes.RED + "Cannot remove from empty cart " + ColorCodes.RESET);
            return;
        }
        String name = "";
        while (!name.equalsIgnoreCase("-1")) {
            System.out.println("Enter product name to be removed from cart ( '-1' to go back)");
            name = sc.nextLine();
            if (name.equalsIgnoreCase("-1")) {
                break;
            }
            Response response = cartService.removeFromCart(customer, name);
            printResponseOutput(response);
        }
    }

    private void getAllOrders(Customer customer) {
        Response response = orderService.getAllOrders(customer);
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
        response = orderService.cancelOrder(1, customer, productName, false);
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
        Response response = orderService.cancelOrder(count, customer, wrappers.getProductName(), true);
        printResponseOutput(response);
    }

    private void orderFromCart(Customer customer) {
        List<CartItems> cart = customer.getCart();
        if (cart.isEmpty()) {
            System.out.println(ColorCodes.RED + "Cannot order from empty cart " + ColorCodes.RESET);
            return;
        }

        for(int i=1;i<=cart.size();i++){
            System.out.printf("%d %s", i, cart.get(i-1));
        }



        System.out.print(ColorCodes.BLUE + cart + ColorCodes.RESET);
        System.out.println("Please provide the product which needs to be ordered from cart : ");
        String productName = sc.nextLine();
        boolean exists = (boolean) productService.hasProduct(customer,productName).getData();
        while (!exists) {
            System.out.println(ColorCodes.RED + "Incorrect product name" + ColorCodes.RESET + "\nplease provide the correct product name :");
            System.out.println("Type '-1' to exit");
            productName = sc.nextLine();
            if (productName.trim().equalsIgnoreCase("-1")) {
                break;
            }
            exists = (boolean) productService.hasProduct(customer, productName).getData();
        }
        if (!productName.equalsIgnoreCase("-1")) {
            int quantity = 0;
            while (quantity <= 0) {
                disclaimerForQuantity();
                System.out.println("Please provide the quantity for the product");
                quantity = sc.nextInt();

            }
            Response response = cartService.orderFromCart(customer, productName, quantity, true);
            printResponseOutput(response);
        } else {
            System.out.println(ColorCodes.BLUE + "Going back" + ColorCodes.RESET);
        }
    }

    private void editCart(Customer customer) {
        if (customer.getCart().isEmpty()) {
            System.out.println(ColorCodes.RED + "Cannot edit empty cart " + ColorCodes.RESET);
            return;
        }
        System.out.println(ColorCodes.BLUE + "Your cart : " + ColorCodes.RESET);
        System.out.println(ColorCodes.YELLOW + customer.getCart() + ColorCodes.RESET);
        System.out.println(ColorCodes.GREEN + "Please provide the product name whose quantity you want to update( Type 'back' to go to previous menu) " + ColorCodes.RESET);
        String product = sc.nextLine();

        Response checkResponse = productService.hasProduct(customer, product);
        boolean check = product.equalsIgnoreCase("back") || (boolean) checkResponse.getData();
        while (!check) {
            System.out.println(ColorCodes.RED + "Incorrect product name please enter correct product name " + ColorCodes.RESET);
            product = sc.nextLine();
            checkResponse = productService.hasProduct(customer, product);
            check = product.equalsIgnoreCase("back") || (boolean) checkResponse.getData();
        }
        if (product.equalsIgnoreCase("back")) {
            return;
        }
        System.out.println(ColorCodes.GREEN + "Press 1 to increase quantity of product\nPress 2 to decrease quantity of product" + ColorCodes.RESET);
        String choice = sc.nextLine();
        while (!choice.equalsIgnoreCase("1") && !choice.equalsIgnoreCase("2")) {
            System.out.println(ColorCodes.RED + "You have entered wrong choice please a correct option" + ColorCodes.RESET);
            choice = sc.nextLine();
        }
        if (choice.equalsIgnoreCase("1")) {
            incrementQuantity(customer, product);
        } else if (choice.equalsIgnoreCase("2")) {
            decrementQuantity(customer, product);
        }
    }

    private void disclaimerForQuantity() {
        System.out.println(ColorCodes.RED + "Disclaimer:\n" +
                "Quantity must be greater than 0" + ColorCodes.RESET);
    }

    private void incrementQuantity(Customer customer, String productName) {
        int quantity = getValidInputsForQuantityUpdate();
        Response response = cartService.incrementQuantity(customer, productName, quantity);
        printResponseOutput(response);
    }

    private void decrementQuantity(Customer customer, String productName) {
        int quantity = getValidInputsForQuantityUpdate();
        Response response = cartService.decrementQuantity(customer, productName, quantity);
        printResponseOutput(response);
    }

    private int getValidInputsForQuantityUpdate() {
        System.out.print(ColorCodes.GREEN + "Quantity : " + ColorCodes.RESET);
        int quantity = sc.nextInt();
        while (quantity <= 0) {
            System.out.println(ColorCodes.RED + "Quantity must be greater than zero" + ColorCodes.RESET);
            System.out.println(ColorCodes.GREEN + "Quantity : " + ColorCodes.RESET);
            quantity = sc.nextInt();

        }
        sc.nextLine();
        return quantity;
    }

    private void printResponseOutput(Response response) {
        if (response.getStatus() == ResponseStatus.SUCCESSFUL) {
            System.out.println(ColorCodes.GREEN + response.getData() + ColorCodes.RESET);
        } else if (response.getStatus() == ResponseStatus.ERROR) {
            System.out.println(ColorCodes.RED + response.getError() + ColorCodes.RESET);
        }
    }
}