package ui;

import dao.OrderDao;
import entities.Order;
import entities.User;
import enums.ResponseStatus;
import enums.UserRole;
import service.RestaurantService;
import service.UserService;
import utility.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminUi {

    private final UserService userService;
    private final RestaurantService restaurantService;
    private User loggedInAdmin;
    private final OrderDao orderDao;

    public AdminUi(UserService userService, RestaurantService restaurantService, OrderDao orderDao) {
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.orderDao = orderDao;
    }

    public void adminMenu(Scanner scanner) {
        Response<User> response = null;
        if (loggedInAdmin == null) {
            response = loginAdmin(scanner);
            if (response.getResponseStatus() == ResponseStatus.FAILURE) {
                System.out.println(ColourCodes.RED + "Invalid credentials or not a customer." + ColourCodes.RESET);
                return;
            }
            loggedInAdmin =response.getData();
        }
        boolean isExit = false;
        while (!isExit) {
            List<String> menuItems = new ArrayList<>(List.of(ColourCodes.CYAN + "\nADMIN MENU" + ColourCodes.RESET,
                    "1. Manage Food Items", "2. Display All Users", "3. Display All Orders",
                    "4. Logout"));
            OperationsInfo.displayMenu(menuItems);

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    ManageFoodItemsUi.manageFoodItems(scanner, restaurantService);
                    break;
                case 2:
                    displayAllUsers();
                    break;
                case 3:
                    displayAllOrders();
                    break;
                case 4:
                    logOutAdmin(loggedInAdmin);
                    isExit = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
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
        System.out.println("Admin logged out.");
    }

    private void displayAllUsers() {
        Formatter.tableTemplate(userService.getAllUsers().stream().toList());
    }

    private void displayAllOrders() {
        for (Order order : orderDao.getAllOrders()) {
            System.out.println(OrderFormatter.formatOrderDetails(order));
        }
    }

}