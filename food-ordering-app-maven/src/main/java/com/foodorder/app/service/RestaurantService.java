package com.foodorder.app.service;

import com.foodorder.app.entities.FoodItem;
import com.foodorder.app.entities.Restaurant;
import com.foodorder.app.enums.FoodCategory;
import com.foodorder.app.utility.Response;

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