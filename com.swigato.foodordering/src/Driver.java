
import daoImpl.RepoFoodDao;
import daoImpl.RepoOrderDao;
import daoImpl.RepoRestaurantDao;
import daoImpl.RepoUserDao;

import entities.FoodItem;
import entities.User;
import enums.FoodCategory;
import enums.UserRole;
import serviceImpl.RestaurantServiceImpl;
import serviceImpl.UserServiceImpl;
import ui.AdminUi;
import ui.CustomerUi;
import utility.ColourCodes;
import utility.OperationsInfo;
import utility.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Driver {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        RepoUserDao userDao = new RepoUserDao();
        RepoFoodDao foodDao = new RepoFoodDao();
        RepoOrderDao orderDao = new RepoOrderDao();
        RepoRestaurantDao restaurantDao = new RepoRestaurantDao();

        UserServiceImpl userService = new UserServiceImpl(userDao);
        RestaurantServiceImpl restaurantService = new RestaurantServiceImpl(restaurantDao, foodDao);

        AdminUi adminUi = new AdminUi(userService, restaurantService, orderDao);
        CustomerUi customerUi = new CustomerUi(userService, restaurantService, orderDao);


        User user1 = new User("Chetan", "chetan@gmail.com", "chetan123", UserRole.CUSTOMER);
        User user2 = new User("Saurav", "s", "s", UserRole.CUSTOMER);
        userService.registerUser(user2);
        User adminUser = new User("admin", "admin@gmai.com", "ds@123", UserRole.ADMIN);
        User adminUser2 = new User("a", "a", "a", UserRole.ADMIN);
        userService.registerUser(user1);
        userService.registerUser(adminUser);
        userService.registerUser(adminUser2);

        FoodItem foodItem = new FoodItem("Pasta", 60, FoodCategory.VEG);
        FoodItem foodItem2 = new FoodItem("PaneerTikka", 150, FoodCategory.VEG);
        FoodItem foodItem3 = new FoodItem("ChickenCurry", 130, FoodCategory.NONVEG);
        FoodItem foodItem4 = new FoodItem("DumBiryani", 200, FoodCategory.NONVEG);
        FoodItem foodItem5 = new FoodItem("Lassi", 40, FoodCategory.BEVERAGES);
        FoodItem foodItem6 = new FoodItem("ButterMilk", 40, FoodCategory.BEVERAGES);
        foodDao.addFood(foodItem);
        foodDao.addFood(foodItem2);
        foodDao.addFood(foodItem3);
        foodDao.addFood(foodItem4);
        foodDao.addFood(foodItem5);
        foodDao.addFood(foodItem6);

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
                        adminUi.adminMenu(scanner);
                        break;
                    case 2:
                        customerUi.customerMenu(scanner);
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