package dao;

import entities.FoodItem;
import enums.FoodCategory;

import java.util.List;
import java.util.Optional;

public interface FoodDao {
    void addFood(FoodItem foodItem);

    void addAllFood(List<FoodItem> foodItemsList);

    Optional<FoodItem> getFoodById(int id);

    List<FoodItem> getAllFood();

    List<FoodItem> getFoodByCategory(FoodCategory category);

    void updateFood(FoodItem foodItem);

    void deleteFood(FoodItem foodItem);
}