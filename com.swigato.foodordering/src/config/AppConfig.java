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
import entities.Restaurant;
import entities.User;
import enums.FoodCategory;
import enums.UserRole;
import service.AuthenticationService;
import service.OrderService;
import service.RestaurantService;
import service.UserService;
import serviceImpl.AuthenticationServiceImpl;
import serviceImpl.OrderServiceImpl;
import serviceImpl.RestaurantServiceImpl;
import serviceImpl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

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
            Restaurant data = (Restaurant) restaurantService.getRestaurant().getData();
            int id = data.getId();

            foodItemList.add(new FoodItem("PaneerTikka", 150, FoodCategory.VEG,id));
            foodItemList.add(new FoodItem("ChickenCurry", 130, FoodCategory.NONVEG,id));
            foodItemList.add(new FoodItem("DumBiryani", 200, FoodCategory.NONVEG,id));
            foodItemList.add(new FoodItem("Lassi", 40, FoodCategory.BEVERAGES,id));
            foodItemList.add(new FoodItem("Pasta", 60, FoodCategory.VEG, id));
            foodItemList.add(new FoodItem("ButterMilk", 40, FoodCategory.BEVERAGES,id));

            foodDao.addAllFood(foodItemList);

        } catch (Exception e) {
            System.out.println("An error occurred.");
        }
    }

    public void initializeRestaurant() {
        try {
            List<FoodItem> foodItemList = foodDao.getAllFood();
            Restaurant restaurant1 = new Restaurant("Fast food corner", "shubham@10", foodItemList);
            restaurantDao.addRestaurant(restaurant1);
        } catch (Exception e) {
            System.out.println("An error occurred.");
        }
    }
}
