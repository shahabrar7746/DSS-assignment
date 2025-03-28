package com.foodorder.app.serviceImpl;

import com.foodorder.app.config.AppConfig;
import com.foodorder.app.service.AuthenticationService;
import com.foodorder.app.service.OrderService;
import com.foodorder.app.service.RestaurantService;
import com.foodorder.app.service.UserService;

public class ServiceContainer {
    private static ServiceContainer serviceContainerInstance;

    private final UserService userService;
    private final RestaurantService restaurantService;
    private final OrderService orderService;
    private final AuthenticationService authenticationService;

    private ServiceContainer() {
        AppConfig appConfig = AppConfig.getAppConfig();
        this.userService = appConfig.getUserService();
        this.restaurantService = appConfig.getRestaurantService();
        this.orderService = appConfig.getOrderService();
        this.authenticationService = appConfig.getAuthenticationService();
    }

    public static ServiceContainer getServiceContainerInstance() {
        if (serviceContainerInstance == null) {
            serviceContainerInstance = new ServiceContainer();
        }
        return serviceContainerInstance;
    }

    public RestaurantService getRestaurantService() {
        return restaurantService;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public UserService getUserService() {
        return userService;
    }
}