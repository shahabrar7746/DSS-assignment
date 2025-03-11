package ui;

import entities.FoodItem;
import enums.FoodCategory;
import service.RestaurantService;
import utility.ColourCodes;
import utility.OperationsInfo;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class RestaurantOwnerUi {
    public static void manageFoodItems(Scanner scanner, RestaurantService restaurantService) {
        boolean isExit = false;
        while (!isExit) {
            try {
                List<String> menuItems = new ArrayList<>(List.of(ColourCodes.CYAN + "\nMANAGE FOOD ITEMS" + ColourCodes.RESET,
                        "1. Add Food Item", "2. Remove Food Item", "3. Update Food Item", "4. Display All Food Items",
                        "5. Display Food by Category", "6. Back to Admin Menu"));
                OperationsInfo.displayMenu(menuItems);

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 ->  addFoodItem(scanner, restaurantService);
                    case 2 ->  removeFoodItem(scanner, restaurantService);
                    case 3 ->  updateFoodItem(scanner, restaurantService);
                    case 4 ->  displayAllFoodItems(restaurantService);
                    case 5 ->  displayFoodByCategory(scanner, restaurantService);
                    case 6 ->  isExit = true;
                    default ->   System.out.println("Invalid choice.");
                }

            } catch (IllegalArgumentException | InputMismatchException e) {
                System.out.println("invalid input");
                scanner.nextLine();
            }
        }
    }

    private static void addFoodItem(Scanner scanner, RestaurantService restaurantService) {
        System.out.println("Enter food name:");
        String name = scanner.nextLine();
        System.out.println("Enter food price:");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter food category (VEG/NONVEG/BEVERAGES):");
        String categoryStr = scanner.nextLine().toUpperCase();
        FoodCategory category = FoodCategory.valueOf(categoryStr);

        FoodItem foodItem = new FoodItem(name, price, category);
        restaurantService.addFood(foodItem);
        System.out.println("Food item added.");
    }

    private static void removeFoodItem(Scanner scanner, RestaurantService restaurantService) {
        System.out.println("Enter food name:");
        String foodName = scanner.nextLine();

        FoodItem foodItem = restaurantService.getAllFood()
                .stream()
                .filter(f -> f.getName().equalsIgnoreCase(foodName))
                .findFirst()
                .orElse(null);
        if (foodItem != null) {
            restaurantService.removeFood(foodItem);
            System.out.println("Food item removed.");
        } else {
            System.out.println("Food item not found.");
        }
    }

    private static void updateFoodItem(Scanner scanner, RestaurantService restaurantService) {
        System.out.println("Enter food name:");
        String foodName = scanner.nextLine();

        FoodItem foodItem = restaurantService.getAllFood()
                .stream()
                .filter(f -> f.getName().equalsIgnoreCase(foodName))
                .findFirst()
                .orElse(null);
        if (foodItem != null) {
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
            restaurantService.updateFood(foodItem);
            System.out.println("Food item updated.");
        } else {
            System.out.println("Food item not found.");
        }
    }

    private static void displayAllFoodItems(RestaurantService restaurantService) {
        System.out.println(ColourCodes.CYAN + "\nMENU" + ColourCodes.RESET);
        System.out.println(String.format(ColourCodes.PURPLE + "| %-15s | %-10s | %-10s |" + ColourCodes.RESET, "Food Name", "Item Price", "Food Category"));
        List<FoodItem> foodItems = restaurantService.getAllFood();
        foodItems.forEach(System.out::println);
    }

    private static void displayFoodByCategory(Scanner scanner, RestaurantService restaurantService) {
        System.out.println("Enter category (VEG/NONVEG/BEVERAGES):");
        String categoryStr = scanner.nextLine().toUpperCase();

        FoodCategory category = FoodCategory.valueOf(categoryStr);
        List<FoodItem> foodItems = restaurantService.getFoodByCategory(category);
        foodItems.forEach(System.out::println);
    }
}