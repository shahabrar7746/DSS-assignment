package com.foodorder.app.config;


import com.foodorder.app.dao.RestaurantFood;
import com.foodorder.app.dao.OrderDao;
import com.foodorder.app.dao.RestaurantDao;
import com.foodorder.app.dao.UserDao;
import com.foodorder.app.daoImpl.RestaurantFoodDaoImpl;
import com.foodorder.app.daoImpl.OrderDaoImpl;
import com.foodorder.app.daoImpl.RestaurantDaoImpl;
import com.foodorder.app.daoImpl.UserDaoImpl;
import com.foodorder.app.entities.FoodItem;
import com.foodorder.app.entities.Restaurant;
import com.foodorder.app.entities.User;
import com.foodorder.app.enums.FoodCategory;
import com.foodorder.app.enums.UserRole;
import com.foodorder.app.jdbcImpl.UserDaoJdbcImpl;
import com.foodorder.app.service.AuthenticationService;
import com.foodorder.app.service.OrderService;
import com.foodorder.app.service.RestaurantService;
import com.foodorder.app.service.UserService;
import com.foodorder.app.serviceImpl.AuthenticationServiceImpl;
import com.foodorder.app.serviceImpl.OrderServiceImpl;
import com.foodorder.app.serviceImpl.RestaurantServiceImpl;
import com.foodorder.app.serviceImpl.UserServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AppConfig {
    Logger logger = LogManager.getLogger();
    private static AppConfig appConfigInstance = null;

    //  private final UserDao userDao;
    private final UserDaoJdbcImpl userDao;
    private final RestaurantFood foodDao;
    private final OrderDao orderDao;
    private final RestaurantDao restaurantDao;

    private final UserService userService;
    private final RestaurantService restaurantService;
    private final OrderService orderService;
    private final AuthenticationService authenticationService;

    private final UserDaoJdbcImpl userDaoJdbc;

    private AppConfig() {
        this.userDao = UserDaoJdbcImpl.getUserDaoJdbcImpl();
        //  this.userDao = UserDaoImpl.getUserDaoImpl();
        this.foodDao = RestaurantFoodDaoImpl.getFoodDaoImpl();
        this.orderDao = OrderDaoImpl.getOrderDao();
        this.restaurantDao = RestaurantDaoImpl.getRestaurantDao();

        this.userDaoJdbc = UserDaoJdbcImpl.getUserDaoJdbcImpl();

        this.userService = new UserServiceImpl(userDao);
        this.restaurantService = new RestaurantServiceImpl(restaurantDao, foodDao);
        this.orderService = new OrderServiceImpl(orderDao);
        this.authenticationService = AuthenticationServiceImpl.getAuthenticationService(userDao);
    }

    public static AppConfig getAppConfig() {
        if (appConfigInstance == null)
            appConfigInstance = new AppConfig();

        return appConfigInstance;
    }


    public void initializeUsers() {
//        User user1 = new User("Chetan ", "chetan@gmail.com", "Chetan@123", UserRole.CUSTOMER);
//        User adminUser = new User("Admin", "admin@gmail.com", "Dss@12345", UserRole.ADMIN);
//        authenticationService.registerUser(user1);
//        authenticationService.registerUser(adminUser);
    }

    public void initializeFoodItems() {
        try {
            List<FoodItem> foodItemList = new ArrayList<>();

            foodItemList.add(new FoodItem("PaneerTikka", 150, FoodCategory.VEG, 1001));
            foodItemList.add(new FoodItem("ChickenCurry", 130, FoodCategory.NONVEG, 1001));
            foodItemList.add(new FoodItem("DumBiryani", 200, FoodCategory.NONVEG, 1001));
            foodItemList.add(new FoodItem("Lassi", 40, FoodCategory.BEVERAGES, 1001));
            foodItemList.add(new FoodItem("Pasta", 60, FoodCategory.VEG, 1001));
            foodItemList.add(new FoodItem("ButterMilk", 40, FoodCategory.BEVERAGES, 1001));

            foodDao.addAllFood(foodItemList);

        } catch (Exception e) {
            logger.error("Error while initializing food items:", e);
            System.out.println("Something went wrong.. ");
        }
    }

    public void initializeRestaurant() {
        Restaurant restaurant1 = new Restaurant(1001, "FastFoodCorner");
        restaurantDao.addRestaurant(restaurant1);
    }
}