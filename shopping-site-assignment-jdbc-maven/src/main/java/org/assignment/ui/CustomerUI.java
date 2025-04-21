package org.assignment.ui;

import lombok.AllArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import org.assignment.entities.CartItems;
import org.assignment.entities.Order;
import org.assignment.entities.User;
import org.assignment.enums.OrderStatus;
import org.assignment.enums.ResponseStatus;
import org.assignment.services.CartService;
import org.assignment.services.OrderService;
import org.assignment.services.ProductService;
import org.assignment.util.ColorCodes;
import org.assignment.util.Inputs;
import org.assignment.util.Response;
import org.assignment.wrappers.ProductWrapper;


import java.util.*;
import java.util.List;

@AllArgsConstructor
public class CustomerUI extends UI {
    private final Scanner sc;
    private final OrderService orderService;
    private final ProductService productService;
    private final CartService cartService;

    @Override
    public void initAdminServices(User admin) {
    }

    @Override
    public void initAuthServices() {

    }

    @Override
    public void initCustomerServices(User user) {
        String name = user.getName();
        System.out.println(ColorCodes.GREEN + "********WELCOME-" + name + "*******" + ColorCodes.RESET);
        System.out.println(ColorCodes.GREEN + "******BROWSE*******" + ColorCodes.RESET);

        String operation = "";
        List<String> options = new ArrayList<>();

        while (!operation.equalsIgnoreCase("0")) {
            boolean isCartEmpty = user.getCart().isEmpty();
            if (!isCartEmpty) {
                options.add("PRESS 1 TO PLACE ORDER");
            }
            options.add("PRESS 2 TO GO TO CART");
            options.add("PRESS 3 TO VIEW ORDERS");
            options.add("PRESS 4 TO CANCEL ORDER");
            if (!isCartEmpty) {
                options.add("PRESS 5 TO ORDER ITEMS FROM CART");
            }
            options.add("TYPE 0 TO LOG OUT");
            options.add("Operation : ");
            displayOptions(options);

            operation = Inputs.getStringInputs(sc).toUpperCase();

            switch (operation) {
                case "1" -> {
                    if (isCartEmpty) {
                        System.out.println(ColorCodes.RED + "Invalid choice" + ColorCodes.RESET);
                    } else {
                        placeOrder(user);
                    }
                }
                case "2" -> goToCart(user);
                case "3" -> getAllOrders(user);
                case "4" -> cancelOrder(user);
                case "5" -> {
                    if (isCartEmpty) {
                        System.out.println(ColorCodes.RED + "Invalid choice" + ColorCodes.RESET);
                    } else {
                        orderFromCart(user);
                    }
                }
                case "0" -> System.out.println("Going back");
                default -> System.out.println("Invalid input");
            }
        }

    }

    private void placeOrder(User user) {
        if (user.getCart().isEmpty()) {
            System.out.println(ColorCodes.RED + "Cannot place order from empty cart " + ColorCodes.RESET);
            return;
        }

        printCartItems(user.getCart());
        System.out.println(ColorCodes.GREEN + "Enter '1' to place orders\n'2' to return main menu\nEnter choice : " + ColorCodes.RESET);
        String operation = Inputs.getStringInputs(sc);
        while (!operation.equalsIgnoreCase("1") && !operation.equalsIgnoreCase("2")) {

            System.out.println(ColorCodes.RED + "Incorrect choice\n Enter again" + ColorCodes.RESET);
            System.out.println(ColorCodes.GREEN + "Enter '1' to place orders\n      '2' to return main menu\nEnter choice : " + ColorCodes.RESET);
            operation = Inputs.getStringInputs(sc);
        }
        if (operation.equalsIgnoreCase("1")) {
            printResponse(orderService.orderCart(user));
        }

    }

    private void goToCart(User user) {
        String choice = "";
        while (!choice.equalsIgnoreCase("5")) {
            Response wrapperResponse = productService.getAllProduct();
            if (wrapperResponse.getStatus() == ResponseStatus.SUCCESSFUL && wrapperResponse.getData() instanceof List<?> wrappers) {
                List<ProductWrapper> wrappers1 = (List<ProductWrapper>) wrappers;
                printProducts(wrappers1);
            } else {
                System.out.println(ColorCodes.GREEN + wrapperResponse.getError() + ColorCodes.RESET);
            }
            List<String> options = new ArrayList<>();
            options.add("PRESS 1 to ADD ITEMS TO CART");
            if (Boolean.FALSE.equals(user.getCart().isEmpty())) {
                System.out.println(ColorCodes.BLUE + "Cart : " + ColorCodes.RESET);
                printCartItems(user.getCart());
                printResponse(cartService.getTotalBillFromCart(user));
                options.add("PRESS 2 TO REMOVE ITEMS FROM CART");
                options.add("PRESS 3 TO EDIT CART");
                options.add("PRESS 4 TO CHECKOUT");
            }
            options.add("PRESS 5 TO EXIT CART");
            options.add(ColorCodes.GREEN + "Operation : " + ColorCodes.RESET);
            displayOptions(options);
            choice = Inputs.getStringInputs(sc).toUpperCase();

            if (choice.equalsIgnoreCase("1")) {
                addToCart(user);
            } else if (choice.equalsIgnoreCase("2")) {
                removeFromCart(user);
            } else if (choice.equalsIgnoreCase("3")) {
                editCart(user);
            } else if (choice.equalsIgnoreCase("4")) {
                chechkOut(user);
            } else if (choice.equalsIgnoreCase("5")) {
                return;
            } else {
                System.out.println(ColorCodes.RED + "Invalid Operation" + ColorCodes.RESET);
            }
        }
    }

    private void addToCart(User user) {
        System.out.println("Please provide product name : ");
        String product = Inputs.getStringInputs(sc).toUpperCase();
        int quantity = 0;
        while (quantity <= 0) {
            System.out.println("Please provide the quantity for the product (press '-1' to go to back)");
            try {
                quantity = Inputs.getIntegerInput(sc);

                if (quantity == -1) {
                    return;
                }
                if (quantity <= 0) {
                    disclaimerForQuantity();
                }
            } catch (InputMismatchException e) {
                System.out.println(ColorCodes.RED + "Inputs must be in Integer only" + ColorCodes.RESET);
                sc.next();
            }
        }

        Response response = cartService.intiateCart(user, product, quantity);
        printResponse(response);
    }

    private void chechkOut(User user) {
        if (user.getCart().isEmpty()) {
            System.out.println(ColorCodes.RED + "Cart is empty " + ColorCodes.RESET);
            return;
        }
        printResponse(orderService.orderCart(user));
    }

    private void removeFromCart(User user) {
        if (user.getCart().isEmpty()) {
            System.out.println(ColorCodes.RED + "Cannot remove from empty cart " + ColorCodes.RESET);
            return;
        }
        printCartItems(user.getCart());
        String name = "";
        while (!name.equalsIgnoreCase("-1") && !user.getCart().isEmpty()) {
            System.out.println("Enter product name to be removed from cart ( '-1' to go back)");
            name = Inputs.getStringInputs(sc);
            if (name.equalsIgnoreCase("-1")) {
                break;
            }
            Response response = cartService.removeFromCartByProductName(user, name);
            printResponse(response);
        }
    }

    private void getAllOrders(User user) {
        Response response = orderService.getAllOrdersByCustomer(user);
        if (response.getStatus() == ResponseStatus.ERROR) {
            printResponse(response);
            return;
        }
        printOrdersForRole(false, (List<Order>) response.getData());

    }

    private void cancelOrder(User user) {
        Response response = orderService.fetchOrdersByCustomerAndOrderStatus(user, OrderStatus.ORDERED);
        if (response.getStatus() == ResponseStatus.ERROR) {
            printResponse(response);
            return;
        }
        System.out.println("Your orders : ");
        printOrdersForRole(false, (List<Order>) response.getData());
        int index = -1;
        try {
            while (index < 1) {
                System.out.println("Please provide index of order \n" +
                        "Note : Index must be equals or greater than zero\n" +
                        "Choice : ");
                index = Inputs.getIntegerInput(sc);
            }
        }catch (InputMismatchException e)
        {
            System.out.println(ColorCodes.RED + "Inputs must be in Integer only" + ColorCodes.RESET);
            sc.next();
        }
        response = orderService.cancelOrder(user, index-1);
        printResponse(response);
    }

    private void orderFromCart(User user) {
        List<CartItems> cart = user.getCart();
        if (cart.isEmpty()) {
            System.out.println(ColorCodes.RED + "Cannot order from empty cart " + ColorCodes.RESET);
            return;
        }

        for (int i = 1; i <= cart.size(); i++) {
            System.out.printf("%d %s", i, cart.get(i - 1));
        }


        System.out.print(ColorCodes.BLUE + cart + ColorCodes.RESET);
        System.out.println("Please provide the product which needs to be ordered from cart : ");
        String productName = Inputs.getStringInputs(sc);
        boolean exists = (boolean) productService.hasProduct(user, productName).getData();
        while (!exists) {
            System.out.println(ColorCodes.RED + "Incorrect product name" + ColorCodes.RESET + "\nplease provide the correct product name :");
            System.out.println("Type '-1' to exit");
            productName = Inputs.getStringInputs(sc).toUpperCase();
            if (StringUtils.isBlank(productName)) {
                continue;
            }
            if (productName.trim().equalsIgnoreCase("-1")) {
                break;
            }
            exists = (boolean) productService.hasProduct(user, productName).getData();
        }
        if (!productName.equalsIgnoreCase("-1")) {
            int quantity = 0;
            while (quantity <= 0) {
                disclaimerForQuantity();
                System.out.println("Please provide the quantity for the product");
                quantity = Inputs.getIntegerInput(sc);
            }
            Response response = cartService.orderFromCart(user, productName, quantity, true);
            printResponse(response);
        } else {
            System.out.println(ColorCodes.BLUE + "Going back" + ColorCodes.RESET);
        }
    }

    private void editCart(User user) {
        if (user.getCart().isEmpty()) {
            System.out.println(ColorCodes.RED + "Cannot edit empty cart " + ColorCodes.RESET);
            return;
        }
        System.out.println(ColorCodes.BLUE + "Your cart : " + ColorCodes.RESET);
        printCartItems(user.getCart());
        System.out.println(ColorCodes.GREEN + "Please provide the product name whose quantity you want to update( Type 'back' to go to previous menu) " + ColorCodes.RESET);
        String product = Inputs.getStringInputs(sc).toUpperCase();

        Optional<CartItems> cartItemsOptional = (Optional<CartItems>) cartService.findCartItemByName(user, product).getData();

        boolean isBack = Boolean.FALSE.equals(product.equalsIgnoreCase("back"));
        boolean isEmpty = Boolean.TRUE.equals(cartItemsOptional.isEmpty());
        boolean check = Boolean.logicalAnd(isBack, isEmpty);
        while (check) {
            System.out.println(ColorCodes.RED + "Incorrect product name please enter correct product name " + ColorCodes.RESET);
            product = Inputs.getStringInputs(sc).toUpperCase();
            cartItemsOptional = (Optional<CartItems>) cartService.findCartItemByName(user, product).getData();
            isBack = Boolean.FALSE.equals(product.equalsIgnoreCase("back"));
            isEmpty = Boolean.TRUE.equals(cartItemsOptional.isEmpty());
            check = Boolean.logicalAnd(isBack, isEmpty);
        }
        if (product.equalsIgnoreCase("back")) {
            return;
        }
        System.out.println("Current quantity of your product : " + cartItemsOptional.get().getQuantity());
        changeQuantity(user, cartItemsOptional.get());
    }

    private void disclaimerForQuantity() {
        System.out.println(ColorCodes.RED + "Disclaimer:\n" +
                "Quantity must be greater than 0" + ColorCodes.RESET);
    }

    private void changeQuantity(User user, CartItems item) {
        int quantity = 0;
        while (quantity == 0) {
            System.out.println("Do you want remove the product (y/n) ?\nChoice : ");
            String choice = Inputs.getStringInputs(sc);
            if (choice.equalsIgnoreCase("y")) {
                break;
            } else if (choice.equalsIgnoreCase("n")) {
                quantity = getValidInputsForQuantityUpdate();
            }
        }
        Response response = cartService.changeQuantity(user, item, quantity);
        printResponse(response);

    }


    private int getValidInputsForQuantityUpdate() {
        int quantity = -1;
        while (quantity < 0) {
            try {

                System.out.println(ColorCodes.GREEN + "New Quantity : " + ColorCodes.RESET);
                quantity = Inputs.getIntegerInput(sc);
                if (quantity < 0) {
                    System.out.println(ColorCodes.RED + "Quantity must be greater than zero" + ColorCodes.RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(ColorCodes.RED + "Inputs must be Integer" + ColorCodes.RESET);
                sc.next();
            }
        }
        return quantity;
    }
}