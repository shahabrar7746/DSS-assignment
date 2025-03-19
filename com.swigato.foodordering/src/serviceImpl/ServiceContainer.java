package serviceImpl;

import config.AppConfig;

import service.AuthenticationService;
import service.OrderService;
import service.RestaurantService;
import service.UserService;

public class ServiceContainer {
    private static final ServiceContainer serviceContainerInstance = new ServiceContainer();

    private final UserService userService;
    private final RestaurantService restaurantService;
    private final OrderService orderService;
    private final AuthenticationService authenticationService;

    public ServiceContainer() {
        AppConfig appConfig = AppConfig.getAppConfig();
        this.userService = appConfig.getUserService();
        this.restaurantService = appConfig.getRestaurantService();
        this.orderService = appConfig.getOrderService();
        this.authenticationService = appConfig.getAuthenticationService();
    }

    public static ServiceContainer getServiceContainerInstance() {
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