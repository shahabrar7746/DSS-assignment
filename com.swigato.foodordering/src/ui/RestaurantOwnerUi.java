package ui;

import entities.FoodItem;
import entities.Restaurant;
import enums.FoodCategory;
import enums.ResponseStatus;
import service.RestaurantService;
import utility.ColourCodes;
import utility.OperationsInfo;
import utility.Response;


import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class RestaurantOwnerUi {
    private static final Scanner scanner = new Scanner(System.in);

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
                System.out.println("invalid input");
                scanner.nextLine();
            }
        }
    }

    private static void addFoodItem(RestaurantService restaurantService) {
        System.out.println("Enter food name:");
        String name = scanner.nextLine();
        System.out.println("Enter food price:");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter food category (VEG/NONVEG/BEVERAGES):");
        String categoryStr = scanner.nextLine().toUpperCase();
        FoodCategory category = FoodCategory.valueOf(categoryStr);

        Restaurant restaurantData = (Restaurant) restaurantService.getRestaurant().getData();
        int id = restaurantData.getId();

        FoodItem foodItem = new FoodItem(name, price, category, id);
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

        Response allFoodResponse = restaurantService.getAllFood();
        List<FoodItem> foodItems = (List<FoodItem>) allFoodResponse.getData();

        Optional<FoodItem> foodItemOptional = foodItems.stream()
                .filter(f -> f.getName().equalsIgnoreCase(foodName))
                .findFirst();

        if (foodItemOptional.isPresent()) {
            FoodItem foodItem = foodItemOptional.get();
            Response response = restaurantService.removeFood(foodItem);
            if (response.getResponseStatus() == ResponseStatus.SUCCESS) {
                System.out.println(response.getMessage());
            } else {
                System.out.println(response.getMessage());
            }
        }
    }

    private static void updateFoodItem(RestaurantService restaurantService) {
        System.out.println("Enter food name:");
        String foodName = scanner.nextLine();

        Response allFoodResponse = restaurantService.getAllFood();
        List<FoodItem> foodItems = (List<FoodItem>) allFoodResponse.getData();

        Optional<FoodItem> foodItemOptional = foodItems.stream().filter(f -> f.getName().equalsIgnoreCase(foodName))
                .findFirst();
        if (foodItemOptional.isPresent()) {
            FoodItem foodItem = foodItemOptional.get();

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