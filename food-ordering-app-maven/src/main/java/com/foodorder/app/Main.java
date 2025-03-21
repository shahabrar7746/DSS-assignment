package com.foodorder.app;

import com.foodorder.app.config.AppConfig;
import com.foodorder.app.entities.User;
import com.foodorder.app.enums.ResponseStatus;
import com.foodorder.app.enums.UserRole;
import com.foodorder.app.service.AuthenticationService;
import com.foodorder.app.serviceImpl.ServiceContainer;
import com.foodorder.app.ui.AdminUi;
import com.foodorder.app.ui.AuthenticationUi;
import com.foodorder.app.ui.CustomerUi;
import com.foodorder.app.utility.ColourCodes;
import com.foodorder.app.utility.OperationsInfo;
import com.foodorder.app.utility.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    static Logger logger = LogManager.getLogger();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        AppConfig appConfig = AppConfig.getAppConfig();
        ServiceContainer serviceContainer = ServiceContainer.getServiceContainerInstance();

        appConfig.initializeUsers();
        appConfig.initializeFoodItems();
        appConfig.initializeRestaurant();

        AdminUi adminUi = new AdminUi(serviceContainer);
        CustomerUi customerUi = new CustomerUi(serviceContainer);

        int choice = 0;
        while (choice != 4) {
            try {
                logger.info("Application started..");
                OperationsInfo.displayMenu(" WELCOME TO FOOD ORDERING APPLICATION",
                        List.of(" login", " Customer registration", " Exit"));

                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> handleLogin(serviceContainer);
                    case 2 -> customerUi.registerCustomer();
                    case 3 -> System.out.println("Application exiting...");

                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                logger.error("Error in main method: ", e);
                System.out.println(ColourCodes.RED + "Invalid input. Please enter a number." + ColourCodes.RESET);
                scanner.nextLine();
            } catch (NoSuchElementException e) {
                logger.error("Error in main method: ", e);
                System.out.println(ColourCodes.RED + "Invalid input. Please enter a number." + ColourCodes.RESET);
            } catch (Exception e) {
                logger.error("Error in main method: ", e);
                System.out.println(ColourCodes.RED + "Error: something went wrong" + ColourCodes.RESET);
                scanner.nextLine();
            }
        }
    }
    private static void handleLogin(ServiceContainer serviceContainer) {
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        Response response = serviceContainer.getAuthenticationService().loginUser(email, password);

        if (response.getResponseStatus() == ResponseStatus.FAILURE) {
            System.out.println(response.getMessage());
            return;
        }
        User userData = (User)response.getData();
        if (userData != null && userData.getRole() == UserRole.ADMIN){
            AdminUi adminUi = new AdminUi(serviceContainer);
            adminUi.initAdminScreen(response);

        } else if (userData != null && userData.getRole() == UserRole.CUSTOMER) {
            CustomerUi customerUi = new CustomerUi(serviceContainer);
            //customerUi.initCustomerScreen(response);
        }else {
            System.out.println();
        }

    }
}