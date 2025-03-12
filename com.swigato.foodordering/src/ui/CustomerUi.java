package ui;

import entities.FoodItem;
import entities.Order;
import entities.OrderItem;
import entities.User;
import enums.ResponseStatus;
import enums.UserRole;
import service.CustomerService;
import service.OrderService;
import service.RestaurantService;
import service.UserService;
import utility.ColourCodes;
import utility.Formatter;
import utility.OperationsInfo;
import utility.Response;

import java.util.*;

public class CustomerUi extends Ui {

    private final UserService userService;
    private final RestaurantService restaurantService;
    private final OrderService orderService;
    private User loggedInCustomer;
    private CustomerService customerService;

    public CustomerUi(UserService userService, RestaurantService restaurantService, OrderService orderService) {
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.orderService = orderService;
    }

    @Override
    public void initCustomerScreen(Scanner scanner) {
        if (loggedInCustomer == null) {
            Response<User> response = loginAsCustomer(scanner);
            if (response.getResponseStatus() == ResponseStatus.FAILURE) {
                System.out.println(ColourCodes.RED + response.getMessage() + ColourCodes.RESET);
                return;
            }

            System.out.println(ColourCodes.GREEN + response.getMessage() + ColourCodes.RESET);
            loggedInCustomer = response.getData();
            customerService = new CustomerService(loggedInCustomer, orderService);
        }
        boolean isExit = false;

        while (!isExit) {
            try {
                List<String> menuItems = new ArrayList<>(List.of(ColourCodes.CYAN + "\nCUSTOMER MENU" + ColourCodes.RESET, "1. View Menu"
                        , "2. Add to Cart", "3. View Cart", "4. Track Order Status", "5. View order history",
                        "6. Logout")
                );
                OperationsInfo.displayMenu(menuItems);

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> viewMenu();
                    case 2 -> addToCart(scanner);
                    case 3 -> viewCart(scanner);
                    case 4 -> trackOrderStatus(scanner);
                    case 5 -> vieOrderHistory(scanner);
                    case 6 -> {
                        logoutCustomer();
                        isExit = true;
                    }
                    default -> System.out.println("Invalid choice.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    public void registerCustomer(Scanner scanner) {
        try {
            System.out.println("Enter name: ");
            String name = scanner.nextLine();
            System.out.println("Enter email:");
            String email = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();
            UserRole customer = UserRole.CUSTOMER;

            Response<User> userResponse = userService.registerUser(new User(name, email, password, customer));

            if (userResponse.getResponseStatus() == ResponseStatus.SUCCESS) {
                User user = userResponse.getData();
                userService.setLoginStatus(email);
                loggedInCustomer = user;
                customerService = new CustomerService(loggedInCustomer, orderService);
                System.out.println(userResponse.getMessage());
                initCustomerScreen(scanner);

            } else if (userResponse.getResponseStatus() == ResponseStatus.FAILURE) {
                System.out.println(userResponse.getMessage());
            } else {
                System.out.println("Enter correct input ");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(ColourCodes.RED + e.getMessage() + ColourCodes.RESET);
        }
    }

    private Response<User> loginAsCustomer(Scanner scanner) {
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        Response<User> userResponse = userService.loginUser(email, password);
        User customer = userResponse.getData();
        if (customer != null && customer.getRole() == UserRole.CUSTOMER) {
            userService.setLoginStatus(email);
            return new Response<>(customer, ResponseStatus.SUCCESS, "Customer logged in successfully.");
        } else {
            return new Response<>(ResponseStatus.FAILURE, "Invalid credentials or not a customer.\n");
        }
    }

    private void viewMenu() {
        System.out.println(ColourCodes.CYAN + "\nMENU" + ColourCodes.RESET);
        System.out.printf(ColourCodes.PURPLE + "| %-15s | %-10s | %-10s |" + ColourCodes.RESET + "%n", "Food Name", "Item Price", "Food Category");
        List<FoodItem> foodItems = restaurantService.getAllFood();
        foodItems.forEach(System.out::println);
    }

    private void addToCart(Scanner scanner) {
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
                confirmCheckout(scanner, checkEmpty);

            } else if (foodName.equalsIgnoreCase("cart")) {
                viewCart(scanner);

            } else {
                Optional<FoodItem> foodItem = foodItemIsPresent(foodName);
                foodItem.ifPresentOrElse(f -> proceedWithCart(scanner, f), () -> System.out.println("Food item not found."));
            }
        }
    }

    private void proceedWithCart(Scanner scanner, FoodItem item) {
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
        return restaurantService.getAllFood()
                .stream()
                .filter(f -> f.getName().equalsIgnoreCase(foodName))
                .findFirst();
    }

    private void viewCart(Scanner scanner) {
        if (customerService.getCart().isEmpty()) {
            System.out.println("Your cart is empty");
            return;
        }

        System.out.println(ColourCodes.CYAN + "\nCART" + ColourCodes.RESET);
        Formatter.tableTemplate(customerService.getCart().values().stream().toList());
        System.out.println("Total bill: " + customerService.getCart().values().stream().mapToDouble(OrderItem::getTotalPrice).sum());
        System.out.println(ColourCodes.RED + "\nType 'exit' to return to the customer menu" + ColourCodes.RESET
                + ColourCodes.BLUE + "\nType checkout to proceed to checkout: " + ColourCodes.RESET);
        while (true) {
            String options = scanner.nextLine();
            if (options.equalsIgnoreCase("checkout")) {
                proceedWithCheckout(scanner);
                return;
            } else if (options.equalsIgnoreCase("exit")) {
                return;
            } else {
                System.out.println("Enter correct input.");
            }
        }
    }

    private void confirmCheckout(Scanner scanner, boolean checkEmpty) {
        if (checkEmpty) {
            System.out.println("Your cart is empty.");
        } else {
            proceedWithCheckout(scanner);
        }
    }

    private void proceedWithCheckout(Scanner scanner) {
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

    private void trackOrderStatus(Scanner scanner) {
        List<Order> allOrders = orderService.getAllOrders();
        if (allOrders.isEmpty()) {
            System.out.println("You have no current orders.");
            return;
        }

        System.out.println("Enter order ID:");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        Order order = orderService.getOrder(orderId);
        if (order != null) {
            System.out.println("Order status: " + order.getOrderStatus());
        } else {
            System.out.println("Order not found.");
        }
    }

    private void vieOrderHistory(Scanner scanner) {
        List<Order> allOrders = orderService.getAllOrders().stream().filter(order -> order.getCustomer().getId() == loggedInCustomer.getId()).toList();
        Formatter.tableTemplate(allOrders.stream().toList());
        if (allOrders.isEmpty()) {
            System.out.println("You have no past orders.");
            return;
        }

        System.out.println("Enter order id to view order details: \n" +
                ColourCodes.RED + "Or type exit to get back to previous menu: " + ColourCodes.RESET);
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("exit")) {
            return;
        }

        int orderId = Integer.parseInt(confirmation);
        Order order = orderService.getOrder(orderId);

        if (order == null || order.getId() != orderId) {
            System.out.println("Invalid Id!");
        } else {
            List<OrderItem> orderItems = order.getOrderItems();
            Formatter.tableTemplate(orderItems);
        }
    }

    private void logoutCustomer() {
        userService.logoutUser(loggedInCustomer.getEmail());
        loggedInCustomer = null;
        customerService = null;
        System.out.println("Customer logged out.");
    }
}