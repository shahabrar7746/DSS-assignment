package com.foodorder.app.dao;



import com.foodorder.app.entities.User;
import com.foodorder.app.exceptions.FailedToPerformOperation;
import com.foodorder.app.exceptions.ValueAlreadyExistsException;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    void addUser(User user) throws ValueAlreadyExistsException, FailedToPerformOperation;

    Optional<User> getUserByEmail(String email);

    List<User> getAllUsers();

    boolean updateUser(User user);

    Optional<User> deleteUser(User user);
}