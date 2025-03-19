package daoImpl;

import dao.RestaurantDao;
import entities.Restaurant;

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
    public Optional<Restaurant> getRestaurant() {
        if (restaurants.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(restaurants.get(0));
        }
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) {
        if (restaurants.isEmpty()) {
            restaurants.add(restaurant);
        } else {
            restaurants.set(0, restaurant);
        }
    }
}
