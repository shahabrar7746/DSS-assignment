package com.foodorder.app.dao;

import com.foodorder.app.entities.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantDao {

    void addRestaurant(Restaurant restaurant);

   Optional<Restaurant> getRestaurantById(int restaurantId);

   List<Restaurant> getAllRestaurants();

}
