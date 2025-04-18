package com.foodorder.app.daoImpl;

import com.foodorder.app.dao.RestaurantFood;
import com.foodorder.app.entities.FoodItem;
import com.foodorder.app.enums.FoodCategory;
import com.foodorder.app.exceptions.FailedToPerformOperation;
import com.foodorder.app.exceptions.ValueAlreadyExistsException;

import java.util.*;

public class RestaurantFoodDaoImpl implements RestaurantFood {
   private final List<FoodItem> foodItems = new ArrayList<>();
    private static final RestaurantFoodDaoImpl foodDao = new RestaurantFoodDaoImpl();

    private RestaurantFoodDaoImpl() {
    }
    //getFoodByRestaurantid //TODO

    public static RestaurantFoodDaoImpl getFoodDaoImpl(){
        return foodDao;
    }

    public void addAllFood(List<FoodItem> foodItemsList) throws ValueAlreadyExistsException, FailedToPerformOperation {
        if (foodItemsList.stream().anyMatch(foodItems::contains)) {
            throw new ValueAlreadyExistsException("Food already exists");
        }

        boolean success = foodItems.addAll(foodItemsList);
        if (!success) {
            throw new FailedToPerformOperation("Unable to add food");
        }
    }

    @Override
    public void addFood(FoodItem foodItem) throws ValueAlreadyExistsException, FailedToPerformOperation {
        if (foodItems.contains(foodItem)) {
            throw new ValueAlreadyExistsException("Food already exists");
        }

        boolean success = foodItems.add(foodItem);
        if (!success) {
            throw new FailedToPerformOperation("Unable to add food");
        }
    }

    @Override
    public Optional<FoodItem> getFoodById(int id) {
        return foodItems.stream()
                .filter(f -> f.getId() == id)
                .findFirst();
    }

    @Override
    public List<FoodItem> getAllFood() {
        if (foodItems == null || foodItems.isEmpty()) {
            return Collections.emptyList();
        }
        return List.copyOf(foodItems);
    }

    @Override
    public List<FoodItem> getFoodByCategory(FoodCategory category) {
        if (foodItems == null || foodItems.isEmpty()) {
            return Collections.emptyList();
        }
        return foodItems.stream()
                .filter(f -> f.getCategory() == category)
                .toList();
    }

    @Override
    public boolean updateFood(FoodItem foodItem) {
        Optional<FoodItem> getOptionalFood = foodItems.stream()
                .filter(f -> f.getId() == foodItem.getId())
                .findFirst();

        if (getOptionalFood.isPresent()) {
            FoodItem f = getOptionalFood.get();
            f.setName(foodItem.getName());
            f.setPrice(foodItem.getPrice());
            f.setCategory(foodItem.getCategory());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteFood(String foodItem) throws FailedToPerformOperation {
        FoodItem itemToDelete = foodItems.stream()
                .filter(f -> f.getName().equals(foodItem))
                .findFirst()
                .orElseThrow(() -> new FailedToPerformOperation("Food item not found!"));

        foodItems.remove(itemToDelete);
        return true;
    }
}