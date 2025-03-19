package dao;

import entities.FoodItem;
import enums.FoodCategory;
import exceptions.FailedToPerformOperation;
import exceptions.ValueAlreadyExistsException;

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