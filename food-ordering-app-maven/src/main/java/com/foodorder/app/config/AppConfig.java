package com.foodorder.app.config;


import com.foodorder.app.dao.FoodDao;
import com.foodorder.app.dao.OrderDao;
import com.foodorder.app.dao.RestaurantDao;
import com.foodorder.app.dao.UserDao;
import com.foodorder.app.daoImpl.FoodDaoImpl;
import com.foodorder.app.daoImpl.OrderDaoImpl;
import com.foodorder.app.daoImpl.RestaurantDaoImpl;
import com.foodorder.app.daoImpl.UserDaoImpl;
import com.foodorder.app.entities.FoodItem;
import com.foodorder.app.entities.Restaurant;
import com.foodorder.app.entities.User;
import com.foodorder.app.enums.FoodCategory;
import com.foodorder.app.enums.UserRole;
import com.foodorder.app.service.AuthenticationService;
import com.foodorder.app.service.OrderService;
import com.foodorder.app.service.RestaurantService;
import com.foodorder.app.service.UserService;
import com.foodorder.app.serviceImpl.AuthenticationServiceImpl;
import com.foodorder.app.serviceImpl.OrderServiceImpl;
import com.foodorder.app.serviceImpl.RestaurantServiceImpl;
import com.foodorder.app.serviceImpl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppConfig {
    private static final AppConfig appConfigInstance = new AppConfig();

    private final UserDao userDao;
    private final FoodDao foodDao;
    private final OrderDao orderDao;
    private final RestaurantDao restaurantDao;

    private final UserService userService;
    private final RestaurantService restaurantService;
    private final OrderService orderService;
    private final AuthenticationService authenticationService;

    private AppConfig() {
        this.userDao = UserDaoImpl.getUserDaoImpl();
        this.foodDao = FoodDaoImpl.getFoodDaoImpl();
        this.orderDao = OrderDaoImpl.getOrderDao();
        this.restaurantDao = RestaurantDaoImpl.getRestaurantDao();

        this.userService = new UserServiceImpl(userDao);
        this.restaurantService = new RestaurantServiceImpl(restaurantDao, foodDao);
        this.orderService = new OrderServiceImpl(orderDao);
        this.authenticationService = AuthenticationServiceImpl.getAuthenticationService(userDao);
    }

    public static AppConfig getAppConfig() {
        return appConfigInstance;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public FoodDao getFoodDao() {
        return foodDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public RestaurantDao getRestaurantDao() {
        return restaurantDao;
    }

    public UserService getUserService() {
        return userService;
    }

    public RestaurantService getRestaurantService() {
        return restaurantService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void initializeUsers() {
        User user1 = new User("Chetan", "chetan@gmail.com", "chetan@123", UserRole.CUSTOMER);
        User adminUser = new User("admin", "admin@gmail.com", "ds@123", UserRole.ADMIN);
        authenticationService.registerUser(user1);
        authenticationService.registerUser(adminUser);
    }

    public void initializeFoodItems() {
        try {
            List<FoodItem> foodItemList = new ArrayList<>();

            foodItemList.add(new FoodItem("PaneerTikka", 150, FoodCategory.VEG));
            foodItemList.add(new FoodItem("ChickenCurry", 130, FoodCategory.NONVEG));
            foodItemList.add(new FoodItem("DumBiryani", 200, FoodCategory.NONVEG));
            foodItemList.add(new FoodItem("Lassi", 40, FoodCategory.BEVERAGES));
            foodItemList.add(new FoodItem("Pasta", 60, FoodCategory.VEG));
            foodItemList.add(new FoodItem("ButterMilk", 40, FoodCategory.BEVERAGES));

            foodDao.addAllFood(foodItemList);

        } catch (Exception e) {
            System.out.println("An error occurred.");
        }
    }

    public void initializeRestaurant() {
        try {
            List<FoodItem> foodItemList = foodDao.getAllFood();
            List<Integer> foodItemIds = foodItemList.stream()
                    .map(FoodItem::getId)
                    .collect(Collectors.toList());
            Restaurant restaurant1 = new Restaurant("Fast food corner", "fastfoodcorner@gmail.com", foodItemIds);
            restaurantDao.addRestaurant(restaurant1);
        } catch (Exception e) {
            System.out.println("An error occurred." + e.getMessage());
        }
    }
}
