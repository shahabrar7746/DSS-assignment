package com.foodorder.app;

import com.foodorder.app.config.AppConfig;
import com.foodorder.app.entities.User;
import com.foodorder.app.enums.ResponseStatus;
import com.foodorder.app.enums.UserRole;
import com.foodorder.app.service.AuthenticationService;
import com.foodorder.app.serviceImpl.ServiceContainer;

import com.foodorder.app.utility.ColourCodes;
import com.foodorder.app.utility.OperationsInfo;
import com.foodorder.app.utility.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    static Logger logger = LogManager.getLogger();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {


        AppConfig appConfig = AppConfig.getAppConfig();
        AuthenticationService authenticationService = appConfig.getAuthenticationService();
        ServiceContainer serviceContainer = ServiceContainer.getServiceContainerInstance();

        appConfig.initializeUsers();
        appConfig.initializeFoodItems();
        appConfig.initializeRestaurant();

        int choice = 0;
        while (choice != 3) {
            try {
                logger.info("Application started..");

                OperationsInfo.displayMenu(" WELCOME TO FOOD ORDERING APPLICATION",
                        List.of(" login", " Customer registration", " Exit"));

                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {

                    case 1 -> handleLoginInput(authenticationService);
                    case 2 -> handleRegister(serviceContainer);
                    case 3 -> System.out.println("Application exiting...");
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (NoSuchElementException e) {
                logger.error("Error in main method: {}", e.getMessage(), e);
                System.out.println(ColourCodes.RED + "Invalid input. Please enter a number." + ColourCodes.RESET);
                scanner.nextLine();
            } catch (Exception e) {
                logger.error("Error in main method: {}", e.getMessage(), e);
                System.out.println(ColourCodes.RED + "Error: something went wrong" + ColourCodes.RESET);
                scanner.nextLine();
            }
        }
    }

    private static void handleLoginInput(AuthenticationService authenticationService) {
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        Response response = authenticationService.handleLoginAuth(email, password);
        System.out.println(response.getMessage());
    }

    private static void handleRegister(ServiceContainer serviceContainer) {
        System.err.println("Press '0' to exit registration!");
        boolean isRegistered = false;

        while (!isRegistered) {
            String name = getInput("Enter name: ");
            if (name == null) return;

            Response validName = serviceContainer.getAuthenticationService().isValidName(name);
            if (Boolean.FALSE.equals(validName.isSuccess())) {
                System.err.println("Error: " + validName.getMessage());
                continue;
            }
            String address = getInput("Enter address: ");
            if (address == null) return;

            String email = getValidEmailInput(serviceContainer);
            if (email == null) return;

            String password = getValidPasswordInput(serviceContainer);
            if (password == null) return;

            User newUser = User.builder().name(name).address(address).email(email).password(password).role(UserRole.CUSTOMER).build();

            Response response = serviceContainer.getAuthenticationService().handleRegisterAuth(newUser);

            if (Boolean.TRUE.equals(response.isSuccess())) {
                System.out.println(ColourCodes.GREEN + response.getMessage() + ColourCodes.RESET);
                isRegistered = true;
            } else {
                System.out.println(ColourCodes.RED + response.getMessage() + ColourCodes.RESET);
            }
        }
    }

    private static String getValidEmailInput(ServiceContainer serviceContainer) {
        while (true) {
            String email = getInput("Enter email: ");
            if (email == null) return null;

            Response validEmail = serviceContainer.getAuthenticationService().isValidEmail(email);
            if (validEmail.getResponseStatus().equals(ResponseStatus.FAILURE)) {
                System.err.println("Error: " + validEmail.getMessage());
                continue;
            }
            return email;
        }
    }

    private static String getValidPasswordInput(ServiceContainer serviceContainer) {
        while (true) {
            String password = getInput("Enter password: ");
            if (password == null) return null;

            Response validPassword = serviceContainer.getAuthenticationService().isValidPassword(password);
            if (validPassword.getResponseStatus().equals(ResponseStatus.FAILURE)) {
                System.err.println("Error: " + validPassword.getMessage());
                continue;
            }
            return password;
        }
    }

    private static String getInput(String prompt) {
        System.out.println(ColourCodes.GREEN + prompt + ColourCodes.RESET);
        String input = scanner.nextLine();
        if (input.equals("0")) {
            System.err.println("Exiting registration..");
            return null;
        }
        return input;
    }
}