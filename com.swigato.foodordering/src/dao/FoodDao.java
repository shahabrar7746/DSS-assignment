package dao;

import entities.FoodItem;
import enums.FoodCategory;

import java.util.List;

public interface FoodDao {
    void addFood(FoodItem foodItem);

    FoodItem getFoodById(int id);

    List<FoodItem> getAllFood();

    List<FoodItem> getFoodByCategory(FoodCategory category);

    void updateFood(FoodItem foodItem);

    void deleteFood(FoodItem foodItem);
}