package daoImpl;

import dao.RestaurantDao;
import entities.Restaurant;

public class RestaurantDaoImpl implements RestaurantDao {
    private Restaurant restaurant;

    @Override
    public Restaurant getRestaurant() {
        return restaurant;
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
