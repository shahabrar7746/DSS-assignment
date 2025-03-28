package com.foodorder.app.ui;

import com.foodorder.app.entities.FoodItem;
import com.foodorder.app.entities.Restaurant;
import com.foodorder.app.enums.FoodCategory;
import com.foodorder.app.enums.ResponseStatus;
import com.foodorder.app.service.RestaurantService;
import com.foodorder.app.utility.ColourCodes;
import com.foodorder.app.utility.OperationsInfo;
import com.foodorder.app.utility.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ManageFoodItemsUi {
    private static final Scanner scanner = new Scanner(System.in);
    private static Logger logger = LogManager.getLogger();

    public static void manageFoodItems(RestaurantService restaurantService) {
        boolean isExit = false;
        while (!isExit) {
            try {
                OperationsInfo.displayMenu("MANAGE FOOD ITEMS", List.of(
                        " Add Food Item", " Remove Food Item", " Update Food Item", " Display All Food Items",
                        " Display Food by Category", " Back to Admin Menu"));

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> addFoodItem(restaurantService);
                    case 2 -> removeFoodItem(restaurantService);
                    case 3 -> updateFoodItem(restaurantService);
                    case 4 -> displayAllFoodItems(restaurantService);
                    case 5 -> displayFoodByCategory(restaurantService);
                    case 6 -> isExit = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (IllegalArgumentException | InputMismatchException e) {
                logger.error("Error from restaurant food Ui: ", e);
                System.out.println("invalid input");
                scanner.nextLine();
            }
        }
    }

    private static void addFoodItem(RestaurantService restaurantService) {
        System.out.println("Enter food name: ");
        String name = scanner.nextLine();
        System.out.println("Enter food price:");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter food category (VEG/NONVEG/BEVERAGES):");
        String categoryStr = scanner.nextLine().toUpperCase();
        FoodCategory category = FoodCategory.valueOf(categoryStr);

        Restaurant restaurantData = (Restaurant) restaurantService.getRestaurant().getData();
        int restaurantDataId = restaurantData.getId();

        FoodItem foodItem = new FoodItem(name, price, category,restaurantDataId);
        Response response = restaurantService.addFood(foodItem);
        if (response.getResponseStatus() == ResponseStatus.SUCCESS) {
            System.out.println(response.getMessage());
        } else {
            System.out.println(response.getMessage());
        }
    }

    private static void removeFoodItem(RestaurantService restaurantService) {
        System.out.println("Enter food name:");
        String foodName = scanner.nextLine();
        Response response = restaurantService.removeFood(foodName);
            if (response.getResponseStatus() == ResponseStatus.SUCCESS) {
                System.out.println(response.getMessage());
            } else {
                System.out.println(response.getMessage());
            }
        }

    private static void updateFoodItem(RestaurantService restaurantService) {
        System.out.println("Enter food name:");
        String foodName = scanner.nextLine();

        Response foodItemByNameOptional = restaurantService.getFoodByName(foodName);
        if (foodItemByNameOptional.getResponseStatus() == ResponseStatus.FAILURE) {
            System.out.println(foodItemByNameOptional.getMessage());
            return;
        }
        FoodItem foodItem = (FoodItem) foodItemByNameOptional.getData();

        System.out.println("Enter new name:");
        String name = scanner.nextLine();
        System.out.println("Enter new price:");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter new category (VEG/NONVEG/BEVERAGES):");
        String categoryStr = scanner.nextLine().toUpperCase();
        FoodCategory category = FoodCategory.valueOf(categoryStr);

        foodItem.setName(name);
        foodItem.setPrice(price);
        foodItem.setCategory(category);
        Response response = restaurantService.updateFood(foodItem);
        if (response.getResponseStatus() == ResponseStatus.SUCCESS) {
            System.out.println(response.getMessage());
        } else {
            System.out.println(response.getMessage());
        }
    }

    private static void displayAllFoodItems(RestaurantService restaurantService) {
        System.out.println(ColourCodes.CYAN + "\nMENU" + ColourCodes.RESET);
        System.out.printf(ColourCodes.PURPLE + "| %-15s | %-10s | %-10s |" + ColourCodes.RESET + "%n", "Food Name", "Item Price", "Food Category");
        Response allFoodResponse = restaurantService.getAllFood();
        List<FoodItem> foodItems = (List<FoodItem>) allFoodResponse.getData();
        foodItems.forEach(System.out::println);
    }

    private static void displayFoodByCategory(RestaurantService restaurantService) {
        System.out.println("Enter category (VEG/NONVEG/BEVERAGES):");
        String categoryStr = scanner.nextLine().toUpperCase();

        FoodCategory category = FoodCategory.valueOf(categoryStr);
        Response foodByCategoryResponse = restaurantService.getFoodByCategory(category);
        List<FoodItem> foodItems = (List<FoodItem>) foodByCategoryResponse.getData();
        foodItems.forEach(System.out::println);
    }
}