package com.foodorder.app.service;

import com.foodorder.app.utility.Response;

public interface UserService {
    Response getAllUsers();

    Response setLoginStatus(String email);

    Response logoutUser(String email);
}
