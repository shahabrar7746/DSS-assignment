package config;

import dao.FoodDao;
import dao.OrderDao;
import dao.RestaurantDao;
import dao.UserDao;
import daoImpl.FoodDaoImpl;
import daoImpl.OrderDaoImpl;
import daoImpl.RestaurantDaoImpl;
import daoImpl.UserDaoImpl;
import entities.FoodItem;
import entities.User;
import enums.FoodCategory;
import enums.UserRole;
import service.OrderService;
import service.RestaurantService;
import service.UserService;
import serviceImpl.OrderServiceImpl;
import serviceImpl.RestaurantServiceImpl;
import serviceImpl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class AppConfig {
    private static final AppConfig appConfigInstance = new AppConfig();

    private AppConfig() {
    }

    public static AppConfig getAppConfig() {
        return appConfigInstance;
    }

    public UserDao getUserDao() {
        return UserDaoImpl.getUserDaoImpl();
    }

    public FoodDao getFoodDao() {
        return new FoodDaoImpl();
    }

    public OrderDao getOrderDao() {
        return new OrderDaoImpl();
    }

    public RestaurantDao getRestaurantDao() {
        return new RestaurantDaoImpl();
    }

    public UserService getUserService(UserDao userDao) {
        return new UserServiceImpl(userDao);
    }

    public RestaurantService getRestaurantService(RestaurantDao restaurantDao, FoodDao foodDao) {
        return new RestaurantServiceImpl(restaurantDao, foodDao);
    }

    public OrderService getOrderService(OrderDao orderDao) {
        return new OrderServiceImpl(orderDao);
    }

    public void initializeUsers(UserService userService) {
        User user1 = new User("Chetan", "chetan@gmail.com", "chetan123", UserRole.CUSTOMER);
        //User user2 = new User("Saurav", "s", "s", UserRole.CUSTOMER);
        //userService.registerUser(user2);
        User adminUser = new User("admin", "admin@gmail.com", "ds@123", UserRole.ADMIN);
        //User adminUser2 = new User("a", "a", "a", UserRole.ADMIN);
        userService.registerUser(user1);
        userService.registerUser(adminUser);
        // userService.registerUser(adminUser2);
    }

    public void initializeFoodItems(FoodDao foodDao) {
        List<FoodItem> foodItemList = new ArrayList<>();
        foodItemList.add(new FoodItem("Pasta", 60, FoodCategory.VEG));
        foodItemList.add(new FoodItem("PaneerTikka", 150, FoodCategory.VEG));
        foodItemList.add(new FoodItem("ChickenCurry", 130, FoodCategory.NONVEG));
        foodItemList.add(new FoodItem("DumBiryani", 200, FoodCategory.NONVEG));
        foodItemList.add(new FoodItem("Lassi", 40, FoodCategory.BEVERAGES));
        foodItemList.add(new FoodItem("Pasta", 60, FoodCategory.VEG));
        foodItemList.add(new FoodItem("ButterMilk", 40, FoodCategory.BEVERAGES));

        foodDao.addAllFood(foodItemList);

//        foodDao.addFood(foodItem);
//        foodDao.addFood(foodItem2);
//        foodDao.addFood(foodItem3);
//        foodDao.addFood(foodItem4);
//        foodDao.addFood(foodItem5);
//        foodDao.addFood(foodItem6);
    }
}