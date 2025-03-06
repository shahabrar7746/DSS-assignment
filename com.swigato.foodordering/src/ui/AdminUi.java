package ui;

import dao.OrderDao;
import entities.Order;
import entities.User;
import enums.ResponseStatus;
import enums.UserRole;
import service.OrderService;
import service.RestaurantService;
import service.UserService;
import utility.*;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminUi extends Ui {

    private final UserService userService;
    private final RestaurantService restaurantService;

    @Override
    public void initAdminScreen(Scanner scanner) {
        if (loggedInAdmin == null) {
            Response<User> response = loginAdmin(scanner);
            if (response.getResponseStatus() == ResponseStatus.FAILURE) {
                System.out.println(ColourCodes.RED + "Invalid credentials or not a customer." + ColourCodes.RESET);
                return;
            }
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
                    case 1:
                        addNewAdmin(scanner);
                        break;
                    case 2:
                        displayAllUsers();
                        break;
                    case 3:
                        displayAllOrders();
                        break;
                    case 4:
                        ManageFoodItemsUi.manageFoodItems(scanner, restaurantService);
                        break;
                    case 5:
                        logOutAdmin(loggedInAdmin);
                        isExit = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private User loggedInAdmin;
    private final OrderService orderService;

    public AdminUi(UserService userService, RestaurantService restaurantService, OrderService orderService) {
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.orderService = orderService;
    }

    private void addNewAdmin(Scanner scanner) {

        System.out.println("Enter name: ");
        String name = scanner.nextLine();
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        UserRole admin = UserRole.ADMIN;

        Response<Boolean> userResponse = userService.registerUser(new User(name, email, password, admin));

        if (userResponse.getResponseStatus() == ResponseStatus.SUCCESS) {
            System.out.println("Admin successfully added");
        } else if (userResponse.getResponseStatus() == ResponseStatus.FAILURE) {
            System.out.println("Unable to add admin");
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
            admin.setLoggedIn(true);
            System.out.println("Admin logged in successfully.");
            return new Response<>(admin, ResponseStatus.SUCCESS);
        } else {
            System.out.println("Invalid credentials or not an admin.");
            return new Response<>(ResponseStatus.FAILURE);
        }
    }

    private void logOutAdmin(User admin) {
        admin.setLoggedIn(false);
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