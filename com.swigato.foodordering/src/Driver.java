
import config.AppConfig;
import dao.FoodDao;
import dao.OrderDao;
import dao.RestaurantDao;
import dao.UserDao;
import daoImpl.FoodDaoImpl;
import daoImpl.OrderDaoImpl;
import daoImpl.RestaurantDaoImpl;
import daoImpl.UserDaoImpl;

import entities.FoodItem;
import entities.User;
import enums.FoodCategory;
import enums.UserRole;
import service.OrderService;
import service.RestaurantService;
import service.UserService;
import serviceImpl.RestaurantServiceImpl;
import serviceImpl.UserServiceImpl;
import ui.AdminUi;
import ui.CustomerUi;
import utility.ColourCodes;
import utility.OperationsInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Driver {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AppConfig appConfig = new AppConfig();

        UserDao userDao = appConfig.getUserDao();
        FoodDao foodDao = appConfig.getFoodDao();
        RestaurantDao restaurantDao = appConfig.getRestaurantDao();
        OrderDao orderDao = appConfig.getOrderDao(); //initializing dao's

        UserService userService = appConfig.getUserService(userDao);
        RestaurantService restaurantService = appConfig.getRestaurantService(restaurantDao, foodDao);
        OrderService orderService = appConfig.getOrderService(orderDao); //initializing services

        appConfig.initializeUsers(userService);
        appConfig.initializeFoodItems(foodDao);     //initializing users and food items

        AdminUi adminUi = new AdminUi(userService, restaurantService, orderService);
        CustomerUi customerUi = new CustomerUi(userService, restaurantService, orderService);

        int choice = 0;
        while (choice != 3) {
            try {
                List<String> menuItems = new ArrayList<>(List.of(ColourCodes.CYAN + " WELCOME TO FOOD ORDERING APPLICATION" + ColourCodes.RESET,
                        "---------------------------------------", "Your Logging in as: ",
                        "1.Admin", "2.Customer", "3.Exit"));

                OperationsInfo.displayMenu(menuItems);
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {

                    case 1:
                        adminUi.initAdminScreen(scanner);
                        break;
                    case 2:
                        customerUi.initCustomerScreen(scanner);
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println(ColourCodes.RED + "Invalid input. Please enter a number." + ColourCodes.RESET);
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println(ColourCodes.RED + "An unexpected error occurred: " + e.getMessage() + ColourCodes.RESET);
                e.printStackTrace();
                scanner.nextLine();
            }
        }
        scanner.close();
    }
}