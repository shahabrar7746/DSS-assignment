package com.foodorder.app.service;


import com.foodorder.app.entities.User;
import com.foodorder.app.utility.Response;

public interface AuthenticationService {
    Response loginUser(String email, String password);

    Response registerUser(User user);

    Response updateUser(User user);

    Response deleteUser(User user);

    boolean isValidEmail(String email);

    boolean isValidPassword(String password);

    boolean isValidName(String name);
}