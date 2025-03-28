package com.foodorder.app.service;


import com.foodorder.app.entities.User;
import com.foodorder.app.enums.UserRole;
import com.foodorder.app.serviceImpl.ServiceContainer;
import com.foodorder.app.utility.Response;

public interface AuthenticationService {
    Response handleLoginAuth(String email, String password);

    Response handleRegisterAuth(User user);

    Response loginUser(String email, String password);

    Response registerUser(User user);

    Response isValidEmail(String email);

    Response isValidPassword(String password);

    Response isValidName(String name);

}