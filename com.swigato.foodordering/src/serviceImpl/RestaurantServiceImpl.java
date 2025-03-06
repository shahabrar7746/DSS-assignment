package serviceImpl;

import dao.FoodDao;
import dao.RestaurantDao;
import entities.FoodItem;
import entities.Restaurant;
import enums.FoodCategory;
import service.RestaurantService;

import java.util.List;

public class RestaurantServiceImpl implements RestaurantService {
        private final RestaurantDao restaurantDao;
        private final FoodDao foodDao;

    public RestaurantServiceImpl (RestaurantDao restaurantDao, FoodDao foodDao) {
        this.restaurantDao = restaurantDao;
        this.foodDao = foodDao;
    }

    @Override
    public Restaurant getRestaurant() {
        return restaurantDao.getRestaurant();
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) {
        restaurantDao.updateRestaurant(restaurant);
    }

    @Override
    public void addFood(FoodItem foodItem) {
        foodDao.addFood(foodItem);
        Restaurant restaurant = restaurantDao.getRestaurant();
        if (restaurant != null) {
            restaurant.addFoodItem(foodItem);
            restaurantDao.updateRestaurant(restaurant);
        }
    }

    @Override
    public void removeFood(FoodItem foodItem) {
        foodDao.deleteFood(foodItem);
        Restaurant restaurant = restaurantDao.getRestaurant();
        if (restaurant != null) {
            restaurant.removeFoodItem(foodItem);
            restaurantDao.updateRestaurant(restaurant);
        }
    }

    @Override
    public void updateFood(FoodItem foodItem) {
        foodDao.updateFood(foodItem);
    }

    @Override
    public List<FoodItem> getAllFood() {
        return foodDao.getAllFood();
    }


    @Override
    public List<FoodItem> getFoodByCategory(FoodCategory category) {
        return foodDao.getFoodByCategory(category);
    }

}