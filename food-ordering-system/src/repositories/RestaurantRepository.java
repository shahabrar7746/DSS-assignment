package repositories;

import entities.Restaurant;
import utility.PopulatedFoodData;

public final class RestaurantRepository {
    private static final Restaurant currentRestaurant;

    private RestaurantRepository() {
    }

    static {
        currentRestaurant = new Restaurant("a", "a", PopulatedFoodData.getFoodItems());

    }

    public static Restaurant getRestaurant() {
        return currentRestaurant;
    }

}