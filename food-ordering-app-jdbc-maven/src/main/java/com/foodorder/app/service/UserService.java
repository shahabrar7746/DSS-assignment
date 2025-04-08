package com.foodorder.app.service;

import com.foodorder.app.utility.Response;

import java.sql.SQLException;

public interface UserService {
    Response getAllUsers() throws SQLException;

    Response setLoginStatus(String email);

    Response logoutUser(String email);


}
