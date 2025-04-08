package com.foodorder.app.serviceImpl;


import com.foodorder.app.dao.RestaurantFood;
import com.foodorder.app.dao.RestaurantDao;
import com.foodorder.app.entities.FoodItem;
import com.foodorder.app.entities.Restaurant;
import com.foodorder.app.enums.FoodCategory;
import com.foodorder.app.enums.ResponseStatus;
import com.foodorder.app.exceptions.FailedToPerformOperation;
import com.foodorder.app.exceptions.ValueAlreadyExistsException;
import com.foodorder.app.service.RestaurantService;
import com.foodorder.app.utility.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantDao restaurantDao;
    private final RestaurantFood restaurantFoodDao;
    Response response;
    Logger logger = LogManager.getLogger();

    public RestaurantServiceImpl(RestaurantDao restaurantDao, RestaurantFood foodDao) {
        this.restaurantDao = restaurantDao;
        this.restaurantFoodDao = foodDao;
    }

    @Override
    public Response getRestaurant() {
        Optional<Restaurant> restaurantOptional = restaurantDao.getAllRestaurants().stream().findFirst();
        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            response = new Response(restaurant, ResponseStatus.SUCCESS, "Successfully fetched restaurant.");
        } else {
            response = new Response(ResponseStatus.FAILURE, "Restaurant not found.");
        }
        return response;
    }

    @Override
    public Response addFood(FoodItem foodItem) {
        try {
            restaurantFoodDao.addFood(foodItem);
            response = new Response(Boolean.TRUE, ResponseStatus.SUCCESS, "Food successfully added.");

        } catch (FailedToPerformOperation e) {
            logger.error("Error add food item method : ", e);
            response = new Response(ResponseStatus.FAILURE, "Unable to add food");
        } catch (ValueAlreadyExistsException e) {
            logger.error("Error in add food item method : ", e);
            response = new Response(ResponseStatus.FAILURE, e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error("Error add food item method : ", e);
            response = new Response(ResponseStatus.FAILURE, "Something went wrong");
        }
        return response;
    }

    @Override
    public Response removeFood(String foodItem) {
        if (foodItem == null || foodItem.trim().isEmpty()) {
            return new Response(ResponseStatus.FAILURE, "Food item cannot be empty or null");
        }
        try {
            restaurantFoodDao.deleteFood(foodItem);
            response = new Response(Boolean.TRUE, ResponseStatus.SUCCESS, "Item deleted successfully");
        } catch (FailedToPerformOperation e) {
            logger.error("Error removing food items : ", e);
            response = new Response(ResponseStatus.FAILURE, "Error : Food item not found!");
        } catch (Exception e) {
            logger.error("Error removing food items : Something went wrong ", e);
            response = new Response(ResponseStatus.FAILURE, "An error occurred");
        }

        return response;
    }

    @Override
    public Response updateFood(FoodItem foodItem) {
        boolean isUpdated = restaurantFoodDao.updateFood(foodItem);
        if (isUpdated) {
            response = new Response(Boolean.TRUE, ResponseStatus.SUCCESS, "Updated food successfully");
        } else {
            response = new Response(ResponseStatus.FAILURE, "Error: food not added");
        }
        return response;
    }

    @Override
    public Response getAllFood() {
        List<FoodItem> allFood = restaurantFoodDao.getAllFood();
        if (allFood.isEmpty()) {
            response = new Response(ResponseStatus.FAILURE, "Error : No food found.");
        } else {
            response = new Response(allFood, ResponseStatus.SUCCESS, "Food fetched successfully");
        }
        return response;
    }

    @Override
    public Response getFoodByName(String foodItem) {
        if (foodItem == null || foodItem.trim().isEmpty()) {
            return new Response(ResponseStatus.FAILURE, "Food item cannot be empty or null");
        }
        List<FoodItem> allFood = restaurantFoodDao.getAllFood();
        if (allFood.isEmpty()) {
            logger.warn("No food found in the system.");
            return new Response(ResponseStatus.FAILURE, "Error : No food found");
        }
        Optional<FoodItem> foodItemOptional = allFood.stream()
                .filter(f -> f
                        .getName()
                        .equalsIgnoreCase(foodItem))
                .findFirst();
        if (foodItemOptional.isPresent()) {
            FoodItem foodItem1 = foodItemOptional.get();
            response = new Response(foodItem1, ResponseStatus.SUCCESS, " Food successfully fetched");
        } else {
            response = new Response(ResponseStatus.FAILURE, "Error : No food found");
        }
        return response;
    }

    @Override
    public Response getFoodByCategory(FoodCategory category) {
        List<FoodItem> foodByCategory = restaurantFoodDao.getFoodByCategory(category);
        if (foodByCategory.isEmpty()) {
            response = new Response(ResponseStatus.FAILURE, "Error : No food found.");
        } else {
            response = new Response(foodByCategory, ResponseStatus.SUCCESS, "Food fetched successfully");
        }
        return response;
    }
}