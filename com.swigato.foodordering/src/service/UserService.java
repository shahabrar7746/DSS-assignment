package service;

import entities.User;
import utility.Response;

import java.util.List;

public interface UserService {
    Response<User> registerUser(User user);

    Response<User> loginUser(String email, String password);

    List<User> getAllUsers();

    void updateUser(User user);

    void deleteUser(User user);

    void setLoginStatus(String email);

    void logoutUser(String email);
}
