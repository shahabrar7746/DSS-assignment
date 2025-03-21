package com.foodorder.app.ui;


import com.foodorder.app.entities.FoodItem;
import com.foodorder.app.entities.Order;
import com.foodorder.app.entities.OrderItem;
import com.foodorder.app.entities.User;
import com.foodorder.app.enums.ResponseStatus;
import com.foodorder.app.enums.UserRole;
import com.foodorder.app.service.*;
import com.foodorder.app.serviceImpl.CustomerServiceImpl;
import com.foodorder.app.serviceImpl.ServiceContainer;
import com.foodorder.app.utility.ColourCodes;
import com.foodorder.app.utility.Formatter;
import com.foodorder.app.utility.OperationsInfo;
import com.foodorder.app.utility.Response;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class CustomerUi extends Ui {

    private final UserService userService;
    private final RestaurantService restaurantService;
    private final OrderService orderService;
    private final AuthenticationService authenticationService;
    private User loggedInCustomer;
    private CustomerService customerService;
    private final Scanner scanner = new Scanner(System.in);


    public CustomerUi(ServiceContainer serviceContainer) {
        this.userService = serviceContainer.getUserService();
        this.restaurantService = serviceContainer.getRestaurantService();
        this.orderService = serviceContainer.getOrderService();
        this.authenticationService = serviceContainer.getAuthenticationService();
    }

    @Override
    public void initCustomerScreen() {
        if (loggedInCustomer == null) {
            Response response = loginAsCustomer();
            if (response.getResponseStatus() == ResponseStatus.FAILURE) {
                System.out.println(ColourCodes.RED + response.getMessage() + ColourCodes.RESET);
                return;
            }

            System.out.println(ColourCodes.GREEN + response.getMessage() + ColourCodes.RESET);
            loggedInCustomer = (User) response.getData();
            customerService = new CustomerServiceImpl(loggedInCustomer, orderService);
        }

        boolean isExit = false;
        while (!isExit) {
            try {
                OperationsInfo.displayMenu("CUSTOMER MENU", List.of(" View Menu", " Add to Cart", " View Cart",
                        " Track Order Status", " View order history"," logout"," Back to main menu"));

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> viewMenu();
                    case 2 -> addToCart();
                    case 3 -> viewCart();
                    case 4 -> trackOrderStatus();
                    case 5 -> vieOrderHistory();
                    case 6 -> {
                        logoutCustomer();
                        isExit = true;
                    }
                    case 7 -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    public void registerCustomer() {
        System.out.println("Enter name: ");
        String name = scanner.nextLine();
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        UserRole customer = UserRole.CUSTOMER;

        Response response = authenticationService.registerUser(new User(name, email, password, customer));
        if (response.getResponseStatus() != ResponseStatus.SUCCESS) {
                System.out.println("Error: " + response.getMessage());
            return;
        }

        User user = (User) response.getData();
        if (user != null) {
            Response loginStatusCheckResponse = userService.setLoginStatus(email);
            if (loginStatusCheckResponse.getResponseStatus() != ResponseStatus.SUCCESS) {
                System.out.println(loginStatusCheckResponse.getMessage());
                return;
            }
            loggedInCustomer = user;
            customerService = new CustomerServiceImpl(loggedInCustomer, orderService);
            initCustomerScreen();
        } else {
            System.out.println("Unexpected error occurred : User data missing ");
        }
    }

    private Response loginAsCustomer() {
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        Response userResponse = authenticationService.loginUser(email, password);
        if (userResponse.getResponseStatus() == ResponseStatus.SUCCESS) {
            User customer = (User) userResponse.getData();
            if (customer.getRole() == UserRole.CUSTOMER) {
                Response setLoginStatusResponse = userService.setLoginStatus(email);
                if (setLoginStatusResponse.getResponseStatus() != ResponseStatus.SUCCESS) {
                    System.out.println(setLoginStatusResponse.getMessage());
                    return setLoginStatusResponse;
                }
                return userResponse;
            } else {
                return new Response(ResponseStatus.FAILURE, "Access denied :Only customer can log in.");
            }
        } else {
            return userResponse;
        }
    }

    private void viewMenu() {
        System.out.println(ColourCodes.CYAN + "\nMENU" + ColourCodes.RESET);
        System.out.printf(ColourCodes.PURPLE + "| %-15s | %-10s | %-10s |" + ColourCodes.RESET + "%n", "Food Name", "Item Price", "Food Category");
        Response allFoodResponse = restaurantService.getAllFood();
        List<FoodItem> foodItems = (List<FoodItem>) allFoodResponse.getData();
        foodItems.forEach(System.out::println);
    }

    private void addToCart() {
        while (true) {
            viewMenu();

            System.out.println(ColourCodes.RED + "\nType 'exit' to return to the customer menu" +
                    "\nType 'cart' to jump to the cart: " + ColourCodes.RESET + ColourCodes.BLUE +
                    "\nType checkout to proceed to checkout: " + ColourCodes.RESET +
                    "\nOr enter a food name to add to your cart: ");
            String foodName = scanner.nextLine();

            if (foodName.equalsIgnoreCase("exit")) {
                return;

            } else if (foodName.equalsIgnoreCase("checkout")) {
                boolean checkEmpty = customerService.getCart().isEmpty();
                confirmCheckout(checkEmpty);

            } else if (foodName.equalsIgnoreCase("cart")) {
                viewCart();

            } else {
                Optional<FoodItem> foodItem = foodItemIsPresent(foodName);
                foodItem.ifPresentOrElse(f -> proceedWithCart(f), () -> System.out.println("Food item not found."));
            }
        }
    }

    private void proceedWithCart(FoodItem item) {
        System.out.println("Enter quantity:");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        if (quantity > 0) {
            customerService.addToCart(item, quantity);
            System.out.println("Added to cart.");
            Formatter.tableTemplate(customerService.getCart().values().stream().toList());
            System.out.println("Total bill: " + customerService.getCart().values().stream().mapToDouble(OrderItem::getTotalPrice).sum());
        } else {
            System.out.println("quantity must be greater than 0");
        }
    }

    private Optional<FoodItem> foodItemIsPresent(String foodName) {
        Response allFoodResponse = restaurantService.getAllFood();
        List<FoodItem> foodItems = (List<FoodItem>) allFoodResponse.getData();
        return foodItems.stream()
                .filter(f -> f.getName().equalsIgnoreCase(foodName))
                .findFirst();
    }


    private void viewCart() {
        if (customerService.getCart().isEmpty()) {
            System.out.println("Your cart is empty");
            return;
        }

        System.out.println(ColourCodes.CYAN + "\nCART" + ColourCodes.RESET);
        Formatter.tableTemplate(customerService.getCart().values().stream().toList());
        System.out.println("Total bill: " + customerService.getCart().values().stream().mapToDouble(OrderItem::getTotalPrice).sum());
        System.out.println(ColourCodes.RED + "\nType 'exit' to return to the customer menu" + ColourCodes.RESET
                + ColourCodes.BLUE + "\nType checkout to proceed to checkout: " + ColourCodes.RESET);
        boolean stopExec = true;
        while (stopExec) {
            String options = scanner.nextLine();
            if (options.equalsIgnoreCase("checkout")) {
                proceedWithCheckout();
                stopExec = false;
                return;
            } else if (options.equalsIgnoreCase("exit")) {
                return;
            } else {
                System.out.println("Enter correct input.");
            }
        }
    }

    private void confirmCheckout(boolean checkEmpty) {
        if (checkEmpty) {
            System.out.println("Your cart is empty.");
        } else {
            proceedWithCheckout();
        }
    }

    private void proceedWithCheckout() {
        System.out.println("Do you want to place the order? (y/n)");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("y")) {
            Order order = customerService.placeOrder();
            System.out.println(ColourCodes.BLUE + "Order placed successfully. Order ID: " + order.getId()
                    + " Total bill: " + order.getTotalBillAmount() + ColourCodes.RESET);
        } else {
            System.out.println("Order not placed.");
        }
    }

    private void trackOrderStatus() {
        List<Order> allOrders = orderService.getAllOrders();
        if (allOrders == null || allOrders.isEmpty()) {
            System.out.println("You have no current orders.");
            return;
        }

        System.out.println("Enter order ID:");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        Response responseOrder = orderService.getOrder(orderId);
        if (responseOrder.getResponseStatus() == ResponseStatus.SUCCESS) {
            Order order = (Order) responseOrder.getData();
            System.out.println("Order status: " + order.getOrderStatus());
        } else {
            System.out.println(responseOrder.getMessage());
        }
    }

    private Boolean isUserLoggedIn() {
        if (loggedInCustomer == null) {
            System.out.println("You must need to login");
            return false;
        }
        return true;
    }

    private List<Order> getCustomerOrders() {
        List<Order> allOrders = orderService.getAllOrders();
        if (allOrders == null || allOrders.isEmpty()) {
            System.out.println("You have no past orders");
            return List.of();
        }
        return allOrders.stream()
                .filter(order -> order.getCustomer().getId() == loggedInCustomer.getId())
                .toList();
    }

    private void vieOrderHistory() {
        if (Boolean.TRUE.equals((isUserLoggedIn()))) return;

        List<Order> customerOrders = getCustomerOrders();
        if (customerOrders.isEmpty()) return;

        Formatter.tableTemplate(customerOrders.stream().toList());

        System.out.println("Enter order id to view order details: \n" +
                ColourCodes.RED + "Or type exit to get back to previous menu: " + ColourCodes.RESET);

        String confirmation = scanner.nextLine().trim();
        if (confirmation.equalsIgnoreCase("exit")) {
            return;
        }
        if (!confirmation.matches("\\d+")) {
            System.out.println("Invalid input, Please enter a valid numeric ID.");
            return;
        }
        int orderId = Integer.parseInt(confirmation);

        Response responseOrder = orderService.getOrder(orderId);
        if (responseOrder.getResponseStatus() == ResponseStatus.SUCCESS || responseOrder.getData() != null) {
            Order order = (Order) responseOrder.getData();
            List<OrderItem> orderItems = order.getOrderItems();
            Formatter.tableTemplate(orderItems);
        } else {
            System.out.println(responseOrder.getMessage());
        }
    }

    private void logoutCustomer() {
        Response response = userService.logoutUser(loggedInCustomer.getEmail());
        if (response.getResponseStatus() == ResponseStatus.SUCCESS) {
            loggedInCustomer = null;
            customerService = null;
            System.out.println(response.getMessage());
        } else {
            System.out.println(response.getMessage());
        }
    }
}