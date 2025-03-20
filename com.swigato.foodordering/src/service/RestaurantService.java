package service;

import entities.FoodItem;
import entities.Restaurant;
import enums.FoodCategory;
import utility.Response;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {
    Response getRestaurant();

    Response updateRestaurant(Restaurant restaurant);

    Response addFood(FoodItem foodItem);

    Response removeFood(FoodItem foodItem);

    Response updateFood(FoodItem foodItem);

    Response getAllFood();

    Response getFoodByName(String foodItem);

    Response getFoodByCategory(FoodCategory category);
}