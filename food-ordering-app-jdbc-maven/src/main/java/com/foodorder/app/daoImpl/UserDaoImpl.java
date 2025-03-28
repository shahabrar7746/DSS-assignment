package com.foodorder.app.daoImpl;


import com.foodorder.app.dao.UserDao;
import com.foodorder.app.entities.User;
import com.foodorder.app.exceptions.FailedToPerformOperation;
import com.foodorder.app.exceptions.ValueAlreadyExistsException;

import java.sql.SQLException;
import java.util.*;

public class UserDaoImpl implements UserDao {
    private final List<User> users = new ArrayList<>();

    private static final UserDaoImpl userDao = new UserDaoImpl();

    private UserDaoImpl() {
    }

    public static UserDaoImpl getUserDaoImpl() {
        return userDao;
    }

    @Override
    public void addUser(User user) throws ValueAlreadyExistsException, FailedToPerformOperation {
        if (users.contains(user)) {
            throw new ValueAlreadyExistsException("User already exists");
        }
        boolean success = users.add(user);
        if (!success) {
            throw new FailedToPerformOperation("Unable to register the user.");
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> getAllUsers() {
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }
        return List.copyOf(users);
    }

    @Override
    public boolean setLoginStatus(String email) throws SQLException {
        return false;
    }

    @Override
    public boolean logoutUser(String email) throws SQLException {
        return false;
    }

}
