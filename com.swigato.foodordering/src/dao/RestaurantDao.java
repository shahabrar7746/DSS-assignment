package dao;

import entities.Restaurant;

public interface RestaurantDao {

    Restaurant getRestaurant();

    void updateRestaurant(Restaurant restaurant);
}
