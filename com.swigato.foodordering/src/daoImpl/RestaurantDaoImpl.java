package daoImpl;

import dao.RestaurantDao;
import entities.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestaurantDaoImpl implements RestaurantDao {
    private final List<Restaurant> restaurants = new ArrayList<>();
    private int nextRestaurantId = 1;
    private static final RestaurantDaoImpl restaurantDao = new RestaurantDaoImpl();

    private RestaurantDaoImpl() {
    }

    public static RestaurantDaoImpl getRestaurantDao() {
        return restaurantDao;
    }

    @Override
    public void addRestaurant(Restaurant restaurant) {
        restaurant.setId(nextRestaurantId++);
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

    @Override
    public void updateRestaurant(Restaurant restaurant) { //TODO
        for (int i = 0; i < restaurants.size(); i++) {
            if (restaurants.get(i).getId() == restaurant.getId()) {
                restaurants.set(i, restaurant);
                return;
            }
        }
    }

//
//    @Override
//    public void addRestaurant(Restaurant restaurant) {
//        restaurants.add(restaurant);
//    }
//
//
//    @Override
//    public Optional<Restaurant> getRestaurant() {
//        if (restaurants.isEmpty()) {
//            return Optional.empty();
//        } else {
//            return Optional.of(restaurants.getFirst());
//        }
//    }
//
//    @Override
//    public void updateRestaurant(Restaurant restaurant) {
//        if (restaurants.isEmpty()) {
//            restaurants.add(restaurant);
//        } else {
//            restaurants.set(0, restaurant);
//        }
//    }
}
