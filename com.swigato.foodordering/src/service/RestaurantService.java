package service;

import entities.FoodItem;
import entities.Restaurant;
import enums.FoodCategory;

import java.util.List;

public interface RestaurantService {
    Restaurant getRestaurant();

    void updateRestaurant(Restaurant restaurant);

    void addFood(FoodItem foodItem);

    void removeFood(FoodItem foodItem);

    void updateFood(FoodItem foodItem);

    List<FoodItem> getAllFood();

    List<FoodItem> getFoodByCategory(FoodCategory category);
}