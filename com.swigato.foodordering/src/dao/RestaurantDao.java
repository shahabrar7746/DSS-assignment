package dao;

import entities.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantDao {

    void addRestaurant(Restaurant restaurant);

//    Optional<Restaurant> getRestaurant();

    Optional<Restaurant> getRestaurantById(int restaurantId);

    List<Restaurant> getAllRestaurants();

    void updateRestaurant(Restaurant restaurant);
}
