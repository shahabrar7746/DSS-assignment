package com.foodorder.app.serviceImpl;


import com.foodorder.app.dao.UserDao;
import com.foodorder.app.entities.User;
import com.foodorder.app.enums.ResponseStatus;
import com.foodorder.app.exceptions.FailedToPerformOperation;
import com.foodorder.app.exceptions.ValueAlreadyExistsException;
import com.foodorder.app.jdbcImpl.UserDaoJdbcImpl;
import com.foodorder.app.service.AuthenticationService;
import com.foodorder.app.ui.AdminUi;
import com.foodorder.app.ui.CustomerUi;
import com.foodorder.app.utility.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Pattern;

public class AuthenticationServiceImpl implements AuthenticationService {
  //  private final UserDao userDao;
    private final UserDaoJdbcImpl userDao;
    private static AuthenticationServiceImpl authenticationService;
    private Response response;
    private final Logger logger = LogManager.getLogger();

    public AuthenticationServiceImpl(UserDaoJdbcImpl userDao) {
        this.userDao = userDao;
    }


    public static AuthenticationServiceImpl getAuthenticationService(UserDaoJdbcImpl userDao) {
        if (authenticationService == null) {
            authenticationService = new AuthenticationServiceImpl(userDao);
        }
        return authenticationService;
    }

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])" +            //one digit
                    "(?=.*[a-z])" +           // one uppercase
                    "(?=.*[A-Z])" +            //one lowercase
                    "(?=.*[@#$%^&+=!~])" +     //one special character
                    "(?!.*[\\s]$)(?!^[\\s]).{8,20}$");  //length between 8-20// removed last and front spaces

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+\\.[A-Za-z]*$");

    /*private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]+([\\.][A-Za-z]+)*([ ][A-Za-z]+)*$"); */

    public Response handleRegisterAuth(User user) {
        if (user == null) {
            return new Response(ResponseStatus.FAILURE, "Invalid user credentials.");
        }
        Response registrationResponse = registerUser(user);
        if (registrationResponse.getResponseStatus() == ResponseStatus.FAILURE) {
            return registrationResponse;
        }
        navigateBasedOnRole(user);
        return new Response(ResponseStatus.SUCCESS, "Registration successful");
    }

    public Response registerUser(User user) {
        try {
//            userDao.addUser(user);
            userDao.addUser(user);
            response = new Response(user, ResponseStatus.SUCCESS, "User added successfully");
        } catch (ValueAlreadyExistsException e) {
            logger.error("Error from register user method value already exists: {}", user.getEmail(), e);
            response = new Response(ResponseStatus.FAILURE, "User already exists");
        } catch (FailedToPerformOperation e) {
            logger.error("Error from register user method: {}", user.getEmail(), e);
            response = new Response(ResponseStatus.FAILURE, "Unable to register");
        } catch (IllegalArgumentException e) {
            logger.error("Invalid data provided for registration: {}", user.getEmail(), e);
            response = new Response(ResponseStatus.FAILURE, "Invalid user data.");
        } catch (Exception e) {
            logger.error("Error from register user method: ", e);
            response = new Response(ResponseStatus.FAILURE, "An error occurred during registration");
        }
        return response;
    }

    @Override
    public Response handleLoginAuth(String email, String password) {
        Response success = loginUser(email.toLowerCase(), password);
        if (Boolean.FALSE.equals(success.isSuccess())) {
            return success;
        }
        boolean isSuccessfull = false;
        User user = (User) success.getData();
        if (user != null) {
            navigateBasedOnRole(user);
            isSuccessfull = true;
        }
        return isSuccessfull ? new Response(ResponseStatus.SUCCESS, "Going back to previous menu..")
                : new Response(ResponseStatus.FAILURE, "Invalid user credentials");
    }

    public void navigateBasedOnRole(User userData) {
        switch (userData.getRole()) {
            case ADMIN:
                AdminUi adminUi = new AdminUi(ServiceContainer.getServiceContainerInstance());
                adminUi.initAdminScreen(userData);
                break;
            case CUSTOMER:
                CustomerUi customerUi = new CustomerUi(ServiceContainer.getServiceContainerInstance());
                customerUi.initCustomerScreen(userData);
                break;
            default:
                System.out.println("Unrecognized user role.");
        }
    }

    @Override
    public Response loginUser(String email, String password) {
        try {
            Optional<User> optionalUser = userDao.getUserByEmail(email);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getPassword().equals(password)) {
                    response = new Response(user, ResponseStatus.SUCCESS, "User successfully logged in..");
                } else {
                    logger.warn("Incorrect password attempt for email: {}", email);
                    response = new Response(ResponseStatus.FAILURE, "User credentials are wrong! ");
                }
            } else {
                logger.warn("Login failed: User not found with email: {}", email);
                response = new Response(ResponseStatus.FAILURE, "try again..");
            }
        } catch (SQLException e) {
            logger.error("Error from login user method data while receiving the data from database: ", e);
            response = new Response(ResponseStatus.FAILURE, "An error occurred during login.");
        }
        return response;
    }


    @Override
    public Response isValidEmail(String email) {
        if (email != null && EMAIL_PATTERN.matcher(email).matches()) {
            response = new Response(1, ResponseStatus.SUCCESS, "Email is valid");
        } else {
            response = new Response(ResponseStatus.FAILURE, "Invalid email format. Please ensure the email is like 'example@gmail.com' ");
        }
        return response;
    }

    @Override
    public Response isValidPassword(String password) {
        if (password != null && PASSWORD_PATTERN.matcher(password).matches()) {
            response = new Response(1, ResponseStatus.SUCCESS, "Password is valid");
        } else {
            response = new Response(ResponseStatus.FAILURE, "Password must include 8 to 20 characters at least one uppercase letter, one number, and one special character");
        }
        return response;
    }

    @Override

    public Response isValidName(  String name) {
        Response response1 = new Response(ResponseStatus.FAILURE, "Name must be between 2 and 20 characters and contain only letters, spaces.");
        if (name == null || name.isBlank()) {
            response1 = new Response(ResponseStatus.FAILURE, "Name cannot be empty");
        }
       else if (name.length() >= 2 && name.length() <= 60) {
            response1 = new Response(1, ResponseStatus.SUCCESS, "Name is valid");
        }
        return response1;
    }
}