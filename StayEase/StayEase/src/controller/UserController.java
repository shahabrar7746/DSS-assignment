package controller;

import entity.User;
import service.UserService;

import java.util.List;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void registerUser(User user) {
        userService.registerUser(user);
        System.out.println("\nCongratulations " + user.getName() + "! You can log in now!");
        System.out.println("User Type: " + user.getUserRole());
    }

    public User getUserById(int userId) {
        return userService.getUserById(userId);
    }

    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    public boolean authenticateUser(String email, String password) {
        return userService.authenticateUser(email, password);
    }

    public boolean isEmailExists(String email) {
        return userService.isEmailExists(email);
    }

    public int createUser(User user) {
        return userService.createUser(user);
    }

    public void updateUserToInactive(User user) {
        userService.updateUserToInactive(user);
    }

    public void updateUserToActive(User user) {
        userService.updateUserToActive(user);
    }

    public List<User> getAllStaff() {
        return userService.getAllStaff();
    }

    public List<User> getAllAdmins() {
        return userService.getAllAdmins();
    }
}
