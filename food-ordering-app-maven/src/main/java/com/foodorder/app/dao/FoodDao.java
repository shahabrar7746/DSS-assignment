package com.foodorder.app.dao;

import com.foodorder.app.entities.FoodItem;
import com.foodorder.app.enums.FoodCategory;
import com.foodorder.app.exceptions.FailedToPerformOperation;
import com.foodorder.app.exceptions.ValueAlreadyExistsException;

import java.util.List;
import java.util.Optional;

public interface FoodDao {
    void addFood(FoodItem foodItem) throws ValueAlreadyExistsException, FailedToPerformOperation;

    void addAllFood(List<FoodItem> foodItemsList) throws ValueAlreadyExistsException, FailedToPerformOperation;

    Optional<FoodItem> getFoodById(int id);

    List<FoodItem> getAllFood();

    List<FoodItem> getFoodByCategory(FoodCategory category);

    boolean updateFood(FoodItem foodItem);

    boolean deleteFood(FoodItem foodItem) throws FailedToPerformOperation;
}