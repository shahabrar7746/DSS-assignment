package dao;

import entities.User;
import exceptions.FailedToPerformOperation;
import exceptions.ValueAlreadyExistsException;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    void addUser(User user) throws ValueAlreadyExistsException, FailedToPerformOperation;

    Optional<User> getUserByEmail(String email);

    List<User> getAllUsers();

    boolean updateUser(User user);

    Optional<User> deleteUser(User user);
}