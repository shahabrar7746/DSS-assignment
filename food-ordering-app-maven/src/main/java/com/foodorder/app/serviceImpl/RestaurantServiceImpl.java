package com.foodorder.app.serviceImpl;


import com.foodorder.app.dao.FoodDao;
import com.foodorder.app.dao.RestaurantDao;
import com.foodorder.app.entities.FoodItem;
import com.foodorder.app.entities.Restaurant;
import com.foodorder.app.enums.FoodCategory;
import com.foodorder.app.enums.ResponseStatus;
import com.foodorder.app.exceptions.FailedToPerformOperation;
import com.foodorder.app.exceptions.ValueAlreadyExistsException;
import com.foodorder.app.service.RestaurantService;
import com.foodorder.app.utility.Response;


import java.util.List;
import java.util.Optional;

public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantDao restaurantDao;
    private final FoodDao foodDao;
    Response response;

    public RestaurantServiceImpl(RestaurantDao restaurantDao, FoodDao foodDao) {
        this.restaurantDao = restaurantDao;
        this.foodDao = foodDao;
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
    public Response updateRestaurant(Restaurant restaurant) {
        if (restaurant == null) {
            return new Response(ResponseStatus.FAILURE, "Error: Restaurant cannot be null.");
        }

        restaurantDao.updateRestaurant(restaurant);
        return new Response(ResponseStatus.SUCCESS, "Restaurant updated successfully.");
    }

    @Override
    public Response addFood(FoodItem foodItem) {
        try {
            Optional<Restaurant> optionalOfRestaurant = restaurantDao.getAllRestaurants().stream().findFirst();

            if (optionalOfRestaurant.isPresent()) {
                foodDao.addFood(foodItem);

                Restaurant restaurant = optionalOfRestaurant.get();
                restaurant.getFoodItemIds().add(foodItem.getId());
                restaurantDao.updateRestaurant(restaurant);

                response = new Response(Boolean.TRUE, ResponseStatus.SUCCESS, "Food successfully added.");
            } else {
                response = new Response(ResponseStatus.FAILURE, "Restaurant not found");
            }
        } catch (FailedToPerformOperation e) {
            response = new Response(ResponseStatus.FAILURE, "Unable to add food");
        } catch (ValueAlreadyExistsException e) {
            response = new Response(ResponseStatus.FAILURE, "Food already exists");
        } catch (Exception e) {
            //TODO logger
            response = new Response(ResponseStatus.FAILURE, "An error occurred");
        }
        return response;
    }

    @Override
    public Response removeFood(FoodItem foodItem) {
        try {
            Optional<Restaurant> restaurantOptional = restaurantDao.getAllRestaurants().stream().findFirst();
            if (restaurantOptional.isPresent()) {
                Restaurant restaurant = restaurantOptional.get();

               restaurant.getFoodItemIds().remove(foodItem.getId());
                restaurantDao.updateRestaurant(restaurant);
                foodDao.deleteFood(foodItem);

            response = new Response(Boolean.TRUE, ResponseStatus.SUCCESS, "Item deleted successfully");
            } else {
                response = new Response(ResponseStatus.FAILURE, "Restaurant not found");
            }
        } catch (FailedToPerformOperation e) {
            response = new Response(ResponseStatus.FAILURE, "Error : Food item not found!");
        } catch (Exception e) {
            //TODO LOGGER
            response = new Response(ResponseStatus.FAILURE, "An error occurred");
        }
        return response;
    }

    @Override
    public Response updateFood(FoodItem foodItem) {
        boolean isUpdated = foodDao.updateFood(foodItem);
        if (isUpdated) {
            response = new Response(Boolean.TRUE, ResponseStatus.SUCCESS, "Updated food successfully");
        } else {
            response = new Response(ResponseStatus.FAILURE, "Error: food not added");
        }
        return response;
    }

    @Override
    public Response getAllFood() {
        List<FoodItem> allFood = foodDao.getAllFood();
        if (allFood.isEmpty()) {
            response = new Response(ResponseStatus.FAILURE, "Error : No food found.");
        } else {
            response = new Response(allFood, ResponseStatus.SUCCESS, "Food fetched successfully");
        }
        return response;
    }

    @Override
    public Response getFoodByName(String foodItem) {
        List<FoodItem> allFood = foodDao.getAllFood();
        if (allFood.isEmpty()){
            return new Response(ResponseStatus.FAILURE, "Error : No food found");
        }
        Optional<FoodItem> foodItemOptional = allFood.stream().filter(f -> f.getName().equalsIgnoreCase(foodItem)).findFirst();
        if (foodItemOptional.isPresent()){
            FoodItem foodItem1 = foodItemOptional.get();
            response = new Response(foodItem1, ResponseStatus.SUCCESS, " Food successfully fetched");
        }else {
            response = new Response(ResponseStatus.FAILURE, "Error : No food found");
        }
        return response;
    }

    @Override
    public Response getFoodByCategory(FoodCategory category) {
        List<FoodItem> foodByCategory = foodDao.getFoodByCategory(category);
        if (foodByCategory.isEmpty()) {
            response = new Response(ResponseStatus.FAILURE, "Error : No food found.");
        } else {
            response = new Response(foodByCategory, ResponseStatus.SUCCESS, "Food fetched successfully");
        }
        return response;
    }
}