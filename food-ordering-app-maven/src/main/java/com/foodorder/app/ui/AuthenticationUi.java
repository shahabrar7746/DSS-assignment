package com.foodorder.app.ui;

import com.foodorder.app.entities.User;
import com.foodorder.app.enums.ResponseStatus;
import com.foodorder.app.enums.UserRole;
import com.foodorder.app.service.AuthenticationService;
import com.foodorder.app.service.UserService;
import com.foodorder.app.serviceImpl.ServiceContainer;
import com.foodorder.app.utility.Response;

import java.util.Scanner;

public class AuthenticationUi extends Ui {
    AuthenticationService authenticationService;
    ServiceContainer serviceContainer;
    UserService userService;
    Scanner scanner = new Scanner(System.in);

    public AuthenticationUi(ServiceContainer serviceContainer) {
        this.userService = serviceContainer.getUserService();
        this.serviceContainer = serviceContainer;
        this.authenticationService = serviceContainer.getAuthenticationService();
    }

    public void login() {
        Response response = null;

        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        Response loginUser = authenticationService.loginUser(email, password);
        User data = (User) loginUser.getData();

        if (loginUser.getResponseStatus() == ResponseStatus.FAILURE) {
            System.out.println(loginUser.getMessage());
            return;
        }
        if (data != null && data.getRole() == UserRole.ADMIN) {
            Response setLoginStatusResponse = userService.setLoginStatus(email);
            if (setLoginStatusResponse.getResponseStatus() == ResponseStatus.SUCCESS) {
                response = new Response(data, ResponseStatus.SUCCESS, "Welcome " +
                        data.getName().concat("!"));
            } else {
                response = new Response(ResponseStatus.FAILURE, "Invalid credentials or not an admin.\n");
            }
            AdminUi adminUi = new AdminUi(serviceContainer);
            adminUi.initAdminScreen(response);
//            if (setLoginStatusResponse.getResponseStatus() != (ResponseStatus.SUCCESS)){
//                response = new Response(ResponseStatus.FAILURE, "Invalid credentials or not an admin.\n");
//            }else {
//                data.setLoggedIn(true);
        }else {
            System.out.println(loginUser.getMessage());
        }
    }
}
//User admin = (User) userResponse.getData();
//        if (admin != null && admin.getRole() == UserRole.ADMIN) {
//
//Response setLoginResponse = userService.setLoginStatus(email);
//            if (setLoginResponse.getResponseStatus() != ResponseStatus.SUCCESS) {
//        System.out.println(setLoginResponse.getMessage());
//        return setLoginResponse;
//            }
//                    return new Response(admin, ResponseStatus.SUCCESS, "Welcome " + admin.getName().concat("!"));
//        } else {
//        return new Response(ResponseStatus.FAILURE, "Invalid credentials or not an admin.\n");
//        }