package dao;

import entities.User;
import java.util.List;

public interface UserDao {

    void addUser(User user);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    void updateUser(User user);

    void deleteUser(User user);
}