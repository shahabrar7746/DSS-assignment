package ui;

import entities.User;
import enums.ResponseStatus;
import enums.UserRole;
import service.OrderService;
import service.RestaurantService;
import service.UserService;
import utility.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminUi extends Ui {

    private final UserService userService;
    private final RestaurantService restaurantService;
    private User loggedInAdmin;
    private final OrderService orderService;

    public AdminUi(UserService userService, RestaurantService restaurantService, OrderService orderService) {
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.orderService = orderService;
    }

    @Override
    public void initAdminScreen(Scanner scanner) {
        if (loggedInAdmin == null) {
            Response<User> response = loginAdmin(scanner);
            if (response.getResponseStatus() == ResponseStatus.FAILURE) {
                System.out.println(ColourCodes.RED +response.getMessage()+ ColourCodes.RESET);
                return;
            }
            System.out.println(ColourCodes.GREEN +response.getMessage()+ColourCodes.RESET);
            loggedInAdmin = response.getData();
        }
        boolean isExit = false;
        while (!isExit) {
            try {
                List<String> menuItems = new ArrayList<>(List.of(ColourCodes.CYAN + "\nADMIN MENU" + ColourCodes.RESET,
                        "1. AddAdmin", "2. Display All Users", "3. Display All Orders",
                        "4. Manage Food Items", "5. Logout"));
                OperationsInfo.displayMenu(menuItems);

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> addNewAdmin(scanner);
                    case 2 -> displayAllUsers();
                    case 3 -> displayAllOrders();
                    case 4 -> RestaurantOwnerUi.manageFoodItems(scanner, restaurantService);
                    case 5 -> {
                        logOutAdmin(loggedInAdmin);
                        isExit = true;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void addNewAdmin(Scanner scanner) {
        System.out.println("Enter name: ");
        String name = scanner.nextLine();
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        UserRole admin = UserRole.ADMIN;

        Response<User> userResponse = userService.registerUser(new User(name, email, password, admin));

        if (userResponse.getResponseStatus() == ResponseStatus.SUCCESS) {
            System.out.println(userResponse.getMessage());

        } else if (userResponse.getResponseStatus() == ResponseStatus.FAILURE) {
            System.out.println(userResponse.getResponseStatus() + userResponse.getMessage());
        } else {
            System.out.println("Enter correct input ");
        }
    }

    private Response<User> loginAdmin(Scanner scanner) {
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        Response<User> userResponse = userService.loginUser(email, password);
        User admin = userResponse.getData();
        if (admin != null && admin.getRole() == UserRole.ADMIN) {
            userService.setLoginStatus(email);
            return new Response<>(admin, ResponseStatus.SUCCESS,"Welcome "+ admin.getName().concat("!"));
        } else {
            return new Response<>(ResponseStatus.FAILURE, "Invalid credentials or not an admin.\n");
        }
    }

    private void logOutAdmin(User admin) {
        userService.logoutUser(admin.getEmail());
        loggedInAdmin = null;
        System.out.println("Admin logged out.");
    }

    private void displayAllUsers() {
        Formatter.tableTemplate(userService.getAllUsers().stream().toList());
    }

    private void displayAllOrders() {
        if (orderService.getAllOrders().isEmpty()) {
            System.out.println("No orders yet");
            return;
        }
        orderService.getAllOrders().
                forEach(order -> System.out.println(OrderFormatter.formatOrderDetails(order)));
    }
}