package service;

import entity.User;

import java.util.List;

public interface UserService {
    void registerUser(User user);
    User getUserById(int userId);
    List<User> getAllStaff();
    List<User> getAllAdmins();
    boolean authenticateUser(String email, String password);
    List<User> getAdmins();
    User getUserByEmail(String email);
    boolean isEmailExists(String email);
    int createUser(User user);
    void updateUserToInactive(User user);
    void updateUserToActive(User user);

}
