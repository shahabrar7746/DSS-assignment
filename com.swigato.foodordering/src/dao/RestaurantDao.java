package dao;

import entities.Restaurant;

import java.util.Optional;

public interface RestaurantDao {

    void addRestaurant(Restaurant restaurant);

    Optional<Restaurant> getRestaurant();

    void updateRestaurant(Restaurant restaurant);
}
