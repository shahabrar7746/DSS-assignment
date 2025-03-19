package service;

import entities.User;
import utility.Response;

import java.util.List;

public interface UserService {
    Response getAllUsers();

    Response setLoginStatus(String email);

    Response logoutUser(String email);
}
