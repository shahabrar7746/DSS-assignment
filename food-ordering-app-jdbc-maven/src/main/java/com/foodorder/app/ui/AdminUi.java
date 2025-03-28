package com.foodorder.app.ui;

import com.foodorder.app.entities.Order;
import com.foodorder.app.entities.User;
import com.foodorder.app.enums.ResponseStatus;
import com.foodorder.app.enums.UserRole;
import com.foodorder.app.service.AuthenticationService;
import com.foodorder.app.service.OrderService;
import com.foodorder.app.service.RestaurantService;
import com.foodorder.app.service.UserService;
import com.foodorder.app.serviceImpl.ServiceContainer;
import com.foodorder.app.utility.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AdminUi extends Ui {

    private final UserService userService;
    private final RestaurantService restaurantService;
    private User loggedInAdmin;
    private final OrderService orderService;
    private final AuthenticationService authenticationService;
    private final Scanner scanner = new Scanner(System.in);

    public AdminUi(ServiceContainer serviceContainer) {
        this.userService = serviceContainer.getUserService();
        this.restaurantService = serviceContainer.getRestaurantService();
        this.orderService = serviceContainer.getOrderService();
        this.authenticationService = serviceContainer.getAuthenticationService();
    }

    Logger logger = LogManager.getLogger();

    @Override
    public void initAdminScreen(User userData) {

        Response loginUserAccessResponse = provideLoginUserAccess(userData.getEmail());
        if (loginUserAccessResponse.getResponseStatus() != ResponseStatus.SUCCESS) {
            System.out.println(loginUserAccessResponse.getMessage());
            return;
        }
        loggedInAdmin = userData;
        System.out.println(ColourCodes.GREEN + "Welcome, Admin " + loggedInAdmin.getName() + "!" + ColourCodes.RESET);

        boolean isExit = false;
        while (!isExit) {
            try {
                OperationsInfo.displayMenu("ADMIN MENU", List.of(
                        " AddAdmin", " Display All Users", " Display All Orders",
                        " Manage Food Items", " Logout"));

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> addNewAdmin();
                    case 2 -> displayAllUsers();
                    case 3 -> displayAllOrders();
                    case 4 -> ManageFoodItemsUi.manageFoodItems(restaurantService);
                    case 5 -> {
                        logOutAdmin(loggedInAdmin);
                        isExit = true;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (IllegalArgumentException e) {
                logger.error("Error from initAdminScreen method: ", e);
                System.out.println(e.getMessage());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void addNewAdmin() {
        System.out.println("Enter name: ");
        String name = scanner.nextLine();
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        UserRole admin = UserRole.ADMIN;

        User user = User.builder().name(name).email(email).password(password).role(admin).build();

        Response validName = authenticationService.isValidName(name);
        if (validName.getResponseStatus().equals(ResponseStatus.FAILURE)) {
            System.out.println(validName.getMessage());
            return;
        }
        Response validEmail = authenticationService.isValidEmail(email);
        if (validEmail.getResponseStatus().equals(ResponseStatus.FAILURE)) {
            System.out.println(validEmail.getMessage());
            return;
        }
        Response validPassword = authenticationService.isValidPassword(password);
        if (validPassword.getResponseStatus().equals(ResponseStatus.FAILURE)) {
            System.out.println(validPassword.getMessage());
            return;
        }

        Response userResponse = authenticationService.registerUser(user);
        if (userResponse.getResponseStatus() == ResponseStatus.SUCCESS) {
            System.out.println(userResponse.getMessage());
        } else {
            System.out.println("Error:" + userResponse.getMessage());
        }
    }

    private Response provideLoginUserAccess(String email) {
        return userService.setLoginStatus(email);
    }

    private void logOutAdmin(User admin) {
        Response response = userService.logoutUser(admin.getEmail());
        if (response.getResponseStatus() != ResponseStatus.SUCCESS) {
            System.out.println(response.getMessage());
            return;
        }
        loggedInAdmin = null;
        System.out.println(response.getMessage());
    }

    private void displayAllUsers() throws SQLException {
        Response allUsersResponse = userService.getAllUsers();
        if (allUsersResponse.getResponseStatus() != ResponseStatus.SUCCESS) {
            System.out.println(allUsersResponse.getMessage());
            return;
        }

        List<User> users = (List<User>) allUsersResponse.getData();
        if (users == null || users.isEmpty()) {
            System.out.println("NO users available to display");
            return;
        }
        Formatter.tableTemplate(users);
    }

    private void displayAllOrders() {
        Response allOrdersResponse = orderService.getAllOrders();
        if (allOrdersResponse.getResponseStatus() != ResponseStatus.FAILURE) {
            System.out.println(allOrdersResponse.getMessage());
            return;
        }
        List<Order> allOrders = (List<Order>) allOrdersResponse.getData();
        if (allOrders.isEmpty()) {
            System.out.println("No orders yet");
            return;
        }
        allOrders.
                forEach(order -> System.out.println(OrderFormatter.formatOrderDetails(order)));
    }
}