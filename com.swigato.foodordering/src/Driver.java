
import config.AppConfig;

import serviceImpl.ServiceContainer;
import ui.AdminUi;
import ui.CustomerUi;
import utility.ColourCodes;
import utility.OperationsInfo;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        AppConfig appConfig = AppConfig.getAppConfig();
        ServiceContainer serviceContainer = ServiceContainer.getServiceContainerInstance();

        appConfig.initializeUsers();
        appConfig.initializeFoodItems();
        appConfig.initializeRestaurant( );

        AdminUi adminUi = new AdminUi(serviceContainer);
        CustomerUi customerUi = new CustomerUi(serviceContainer);

        int choice = 0;
        while (choice != 4) {
            try {
                OperationsInfo.displayMenu(" WELCOME TO FOOD ORDERING APPLICATION",
                        List.of(" Admin login", " Customer login", " Customer registration", " Exit"));

                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> adminUi.initAdminScreen();
                    case 2 -> customerUi.initCustomerScreen();
                    case 3 -> customerUi.registerCustomer();
                    case 4 -> System.out.println("Exiting...");
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println(ColourCodes.RED + "Invalid input. Please enter a number." + ColourCodes.RESET);
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println(ColourCodes.RED + "An unexpected error occurred: " + e.getMessage() + ColourCodes.RESET);
                scanner.nextLine();
            }
        }
        scanner.close();
    }
}