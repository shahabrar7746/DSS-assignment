package dao;

import entities.User;
import utility.Response;

import java.util.List;

public interface UserDao {

    Response<Boolean> addUser(User user);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    void updateUser(User user);

    void deleteUser(User user);
}