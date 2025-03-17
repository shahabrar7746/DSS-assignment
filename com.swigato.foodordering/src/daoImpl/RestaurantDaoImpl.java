package daoImpl;

import dao.RestaurantDao;
import entities.Restaurant;

public class RestaurantDaoImpl implements RestaurantDao {
    private Restaurant restaurant;
    private static final RestaurantDaoImpl restaurantDao= new RestaurantDaoImpl();

    private RestaurantDaoImpl(){
    }

    public static RestaurantDaoImpl getRestaurantDao(){
        return restaurantDao;
    }

    @Override
    public Restaurant getRestaurant() {
        return restaurant;
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
