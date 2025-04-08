package com.foodorder.app.daoImpl;


import com.foodorder.app.dao.RestaurantDao;
import com.foodorder.app.entities.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestaurantDaoImpl implements RestaurantDao {
    private final List<Restaurant> restaurants = new ArrayList<>();
    private static final RestaurantDaoImpl restaurantDao = new RestaurantDaoImpl();

    private RestaurantDaoImpl() {
    }

    public static RestaurantDaoImpl getRestaurantDao() {
        return restaurantDao;
    }

    @Override
    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    @Override
    public Optional<Restaurant> getRestaurantById(int restaurantId) {
        return restaurants.stream()
                .filter(r -> r.getId() == restaurantId)
                .findFirst();
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return new ArrayList<>(restaurants);
    }
}
