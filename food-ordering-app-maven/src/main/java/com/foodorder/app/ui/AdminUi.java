package com.foodorder.app.ui;

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
    public void initAdminScreen(Response response) {
        if (response.getResponseStatus() == ResponseStatus.FAILURE) {
            System.out.println(response.getMessage());
            return;
        }
        loggedInAdmin = (User) response.getData();

        Response loginUserAccessResponse = provideLoginUserAccess(loggedInAdmin.getEmail());
        if (loginUserAccessResponse.getResponseStatus() != ResponseStatus.FAILURE) {
            System.out.println(loginUserAccessResponse.getMessage());
            return;
        }
        System.out.println(ColourCodes.GREEN + response.getMessage() + ColourCodes.RESET);

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
                    case 4 -> RestaurantOwnerUi.manageFoodItems(restaurantService);
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

    private void addNewAdmin() {
        System.out.println("Enter name: ");
        String name = scanner.nextLine();
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        UserRole admin = UserRole.ADMIN;

        Response userResponse = authenticationService.registerUser(new User(name, email, password, admin));

        if (userResponse.getResponseStatus() == ResponseStatus.SUCCESS) {
            System.out.println(userResponse.getMessage());
        } else {
            System.out.println("Error:" + userResponse.getMessage());
        }
    }

    private Response provideLoginUserAccess(String email) {
        Response response = userService.setLoginStatus(email);
        if (response.getResponseStatus() = !ResponseStatus.SUCCESS)
            return response;
    }
//    private Response loginAdmin() {
//        System.out.println("Enter email:");
//        String email = scanner.nextLine();
//        System.out.println("Enter password:");
//        String password = scanner.nextLine();
//
//        Response userResponse = authenticationService.loginUser(email, password);
//
//        User admin = (User) userResponse.getData();
//        if (admin != null && admin.getRole() == UserRole.ADMIN) {
//
//            Response setLoginResponse = userService.setLoginStatus(email);
//            if (setLoginResponse.getResponseStatus() != ResponseStatus.SUCCESS) {
//                System.out.println(setLoginResponse.getMessage());
//                return setLoginResponse;
//            }
//            return new Response(admin, ResponseStatus.SUCCESS, "Welcome " + admin.getName().concat("!"));
//        } else {
//            return new Response(ResponseStatus.FAILURE, "Invalid credentials or not an admin.\n");
//        }
//    }

    private void logOutAdmin(User admin) {
        Response response = userService.logoutUser(admin.getEmail());
        if (response.getResponseStatus() != ResponseStatus.SUCCESS) {
            System.out.println(response.getMessage());
            return;
        }
        loggedInAdmin = null;
        System.out.println(response.getMessage());
    }

    private void displayAllUsers() {
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
        if (orderService.getAllOrders().isEmpty()) {
            System.out.println("No orders yet");
            return;
        }
        orderService.getAllOrders().
                forEach(order -> System.out.println(OrderFormatter.formatOrderDetails(order)));
    }
}