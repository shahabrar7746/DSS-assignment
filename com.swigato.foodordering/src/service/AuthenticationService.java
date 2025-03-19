package service;

import entities.User;
import utility.Response;

public interface AuthenticationService {
    Response loginUser(String email, String password);

    Response registerUser(User user);

    Response updateUser(User user);

    Response deleteUser(User user);

    boolean isValidEmail(String email);

    boolean isValidPassword(String password);

    boolean isValidName(String name);
}