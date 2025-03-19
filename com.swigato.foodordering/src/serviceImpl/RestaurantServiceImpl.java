package serviceImpl;

import dao.FoodDao;
import dao.RestaurantDao;
import entities.FoodItem;
import entities.Restaurant;
import enums.FoodCategory;
import enums.ResponseStatus;
import exceptions.FailedToPerformOperation;
import exceptions.ValueAlreadyExistsException;
import service.RestaurantService;
import utility.Response;

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
        Optional<Restaurant> restaurantOptional = restaurantDao.getRestaurant();
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
            Optional<Restaurant> optionalOfRestaurant = restaurantDao.getRestaurant();

            if (optionalOfRestaurant.isPresent()) {
                foodDao.addFood(foodItem);

                Restaurant restaurant = optionalOfRestaurant.get();
                restaurant.addFoodItem(foodItem );
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
            foodDao.deleteFood(foodItem);
            Optional<Restaurant> restaurantOptional = restaurantDao.getRestaurant();
            if (restaurantOptional.isPresent()) {

                Restaurant restaurant = restaurantOptional.get();

                restaurant.removeFoodItem(foodItem);
                restaurantDao.updateRestaurant(restaurant);
            response = new Response(Boolean.TRUE, ResponseStatus.SUCCESS, "Item deleted successfully");
            } else {
                response = new Response(ResponseStatus.FAILURE, "Restaurant not found");
            }
        } catch (FailedToPerformOperation e) {
            response = new Response(ResponseStatus.FAILURE, "Food item not found!");
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
            response = new Response(ResponseStatus.FAILURE, "No food found.");
        } else {
            response = new Response(allFood, ResponseStatus.SUCCESS, "Food fetched successfully");
        }
        return response;
    }

    @Override
    public Response getFoodByCategory(FoodCategory category) {
        List<FoodItem> foodByCategory = foodDao.getFoodByCategory(category);
        if (foodByCategory.isEmpty()) {
            response = new Response(ResponseStatus.FAILURE, "No food found.");
        } else {
            response = new Response(foodByCategory, ResponseStatus.SUCCESS, "Food fetched successfully");
        }
        return response;
    }
}