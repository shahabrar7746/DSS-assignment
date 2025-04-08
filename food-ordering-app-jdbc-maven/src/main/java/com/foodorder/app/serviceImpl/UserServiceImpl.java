package com.foodorder.app.serviceImpl;
import com.foodorder.app.dao.UserDao;
import com.foodorder.app.entities.User;
import com.foodorder.app.enums.ResponseStatus;
import com.foodorder.app.jdbcImpl.UserDaoJdbcImpl;
import com.foodorder.app.service.UserService;
import com.foodorder.app.utility.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
//    private final UserDaoJdbcImpl userDao;
   private final UserDao userDao;
    Response response;
    Logger logger = LogManager.getLogger();


    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Response getAllUsers() {
        Response response = null;
        try {
            List<User> allUsers = userDao.getAllUsers();

            if (allUsers == null || allUsers.isEmpty()) {
                response = new Response(ResponseStatus.FAILURE, "No users found.");
            } else {
                response = new Response(allUsers, ResponseStatus.SUCCESS, "Users fetched successfully");
            }
        } catch (SQLException e) {
            logger.error("Error fetching users", e);
            response = new Response(ResponseStatus.FAILURE, "An error occurred while fetching users.");
        }

        return response;
    }

    @Override
    public Response setLoginStatus(String email) {
        try {
            boolean setLoginStatus = userDao.setLoginStatus(email);
            if (setLoginStatus) {
                return new Response(ResponseStatus.SUCCESS, "Set login success");
            }
        } catch (SQLException e) {
            logger.error("Error from user service while attempting to change the login status: ", e);
        }
        return new Response(ResponseStatus.FAILURE, "Unable to set login status");
    }


    @Override
    public Response logoutUser(String email) {
        try {
            boolean logoutUser = userDao.logoutUser(email);
            if (logoutUser) {
                return new Response(ResponseStatus.SUCCESS, "Logout success");
            }
        } catch (SQLException e) {
            logger.error("Error from user service while attempting to logout: ", e);
        }
        return new Response(ResponseStatus.FAILURE, "Unable to logout customer");
    }
}
