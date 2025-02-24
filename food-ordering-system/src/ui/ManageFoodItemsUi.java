package ui;

import entities.Food;
import entities.Restaurant;
import enums.FoodCategory;
import repositories.RestaurantRepository;
import utility.ColourCodes;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ManageFoodItemsUi {
    public static Restaurant currentRestaurant = RestaurantRepository.getRestaurant();

    public static void manageFoodItems(Scanner scanner) {
        if (Objects.isNull(currentRestaurant)) {
            return;
        }
        int choice;
        boolean isExit = true;
        while (isExit) {
            try {
                System.out.println(ColourCodes.CYAN + "\nMANAGE FOOD-ITEMS \n------------------------" + ColourCodes.RESET);
                System.out.println("1. Add food item");
                System.out.println("2. Remove food item");
                System.out.println("3. Update food item");
                System.out.println("4. Display all food items");
                System.out.println("5. Display food by category");
                System.out.println("6. Back to main menu");
                System.out.println(" Choose an option ");

                choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        addFoodItem(scanner);
                        break;
                    case 2:
                        removeFoodItems(scanner);
                        break;
                    case 3:
                        updateFoodItems(scanner);
                        break;
                    case 4:
                        displayAllFood();
                        break;
                    case 5:
                        displayFoodByCategory(scanner);
                        break;
                    case 6:
                        isExit = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println(ColourCodes.RED + "Invalid input entered." + ColourCodes.RESET);
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private static void addFoodItem(Scanner scanner) {
        try {
            System.out.println("Enter food name: ");
            String foodName = scanner.nextLine();
            System.out.println("Enter food price: ");
            double foodPrice = scanner.nextDouble();

            System.out.println("Enter food category:(VEG/NONVEG/BEVERAGES) ");
            scanner.nextLine();
            String categoryInput = scanner.nextLine();
            FoodCategory category = FoodCategory.valueOf(categoryInput.toUpperCase());

            Food food = new Food(foodName, foodPrice, category);
            currentRestaurant.getFoodItemList().add(food);
            System.out.println("Food item added successfully!");
        } catch (InputMismatchException e) {
            System.out.println(ColourCodes.RED + "Invalid input entered.");
            scanner.nextLine();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid category entered. Please enter either VEG, NONVEG or BEVERAGES");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void removeFoodItems(Scanner scanner) {
        try {
            List<Food> foodList = currentRestaurant.getFoodItemList();
            foodList.forEach(System.out::println);
            System.out.println("Enter food item to remove");
            String name = scanner.nextLine();
            boolean removedItem = foodList.removeIf(food -> food.getName().equalsIgnoreCase(name));
            if (removedItem) {
                System.out.println("Item removed successfully");
            } else {
                System.out.println("Cannot find the item. Please try again!");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void updateFoodItems(Scanner scanner) {
        try {
            List<Food> foodList = currentRestaurant.getFoodItemList();
            foodList.forEach(System.out::println);
            System.out.println("Enter the food item to be updated: ");
            String name = scanner.nextLine();
            System.out.print("Enter new price: ");
            double newPrice = scanner.nextDouble();
            scanner.nextLine();
            boolean found = foodList.stream()
                    .filter(food -> food.getName().equalsIgnoreCase(name))
                    .findFirst()
                    .map(food -> {
                        food.setPrice(newPrice);
                        System.out.println("Price updated successfully.");
                        return true;
                    })
                    .orElse(false);

            if (!found) {
                System.out.println("Food item not found. Try again with the correct name.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input entered.");
            scanner.nextLine();
        }catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void displayFoodByCategory(Scanner scanner) {
        try {
            System.out.print("Enter category (VEG/NONVEG/BEVERAGES): ");
            String categoryInput = scanner.nextLine().toUpperCase();
            FoodCategory category = FoodCategory.valueOf(categoryInput);

            List<Food> foodList = currentRestaurant.getFoodByCategory(category.getCategory());

            if (foodList.isEmpty()) {
                System.out.println("No food items available in this category.");
            } else {
                foodList.forEach(System.out::println);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid category entered. Please enter either VEG, NONVEG or BEVERAGES.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void displayAllFood() {
        try {
            System.out.println("Displaying all food items");
            List<Food> displayFoodList = currentRestaurant.getFoodItemList();
            if (displayFoodList.isEmpty()) {
                System.out.println("No items are available.");
            } else {
                displayFoodList.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println(ColourCodes.RED+"An error occurred: " + e.getMessage()+ColourCodes.RESET );
        }
    }
}