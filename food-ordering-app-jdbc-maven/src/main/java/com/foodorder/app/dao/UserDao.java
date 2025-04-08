package com.foodorder.app.dao;



import com.foodorder.app.entities.User;
import com.foodorder.app.exceptions.FailedToPerformOperation;
import com.foodorder.app.exceptions.ValueAlreadyExistsException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDao {

    void addUser(User user) throws ValueAlreadyExistsException, FailedToPerformOperation, SQLException;

    Optional<User> getUserByEmail(String email) throws SQLException;

    List<User> getAllUsers() throws SQLException;

    boolean setLoginStatus(String email) throws SQLException;

    boolean logoutUser(String email) throws SQLException;

//    boolean updateUser(User user);
//
//    Optional<User> deleteUser(User user);

}