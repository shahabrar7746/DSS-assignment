package serviceImpl;

import dao.UserDao;
import entities.User;
import enums.ResponseStatus;
import enums.UserRole;
import exceptions.FailedToPerformOperation;
import exceptions.ValueAlreadyExistsException;
import service.AuthenticationService;
import utility.ColourCodes;
import utility.Response;

import java.util.Optional;
import java.util.regex.Pattern;

public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserDao userDao;
    private static AuthenticationServiceImpl authenticationService;
    private UserRole role;
    Response response;

    private AuthenticationServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public static AuthenticationServiceImpl getAuthenticationService(UserDao userDao) {
        if (authenticationService == null) {
            authenticationService = new AuthenticationServiceImpl(userDao);
        }
        return authenticationService;
    }


    public Response registerUser(User user) {
        try {
            if (!isValidEmail(user.getEmail()) || !isValidPassword(user.getPassword()) || !isValidName(user.getName())) {
                throw new IllegalArgumentException("Invalid user data.");
            }
            userDao.addUser(user);
            response = new Response(user, ResponseStatus.SUCCESS, "User added successfully");
        } catch (ValueAlreadyExistsException e) {
            response = new Response(ResponseStatus.FAILURE, "User already exists");
        } catch (FailedToPerformOperation e) {
            response = new Response(ResponseStatus.FAILURE, "Unable to register");
        } catch (IllegalArgumentException e) {
            response = new Response(ResponseStatus.FAILURE, "Invalid user data.");
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage()); // TODO
            response = new Response(ResponseStatus.FAILURE, "An error occurred during register");
        }
        return response;
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
                    response = new Response(ResponseStatus.FAILURE, "User credentials are wrong! ");
                }
            } else {
                response = new Response(ResponseStatus.FAILURE, "User not found");
            }
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage()); // TODO
            response = new Response(ResponseStatus.FAILURE, "An error occurred during login.");
        }
        return response;
    }

    @Override
    public Response updateUser(User user) {
        boolean isUpdated = userDao.updateUser(user);
        if (isUpdated) {
            response = new Response(Boolean.TRUE, ResponseStatus.SUCCESS, "User updated successfully");
        } else {
            response = new Response(ResponseStatus.FAILURE, "Error : User not updated");
        }
        return response;
    }

    @Override
    public Response deleteUser(User user) {
        Optional<User> optionalUser = userDao.deleteUser(user);
        return optionalUser.map(value -> response = new Response(value, ResponseStatus.SUCCESS, "User successfully deleted."))
                .orElseGet(() -> new Response(ResponseStatus.FAILURE, "Unable to delete user."));
    }

    @Override
    public boolean isValidEmail(String email) {
        return Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matcher(email).matches();
    }

    @Override
    public boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        final int ADMIN_MIN_LENGTH = 8;
        final int CUSTOMER_MIN_LENGTH = 6;

        int minLength = (role == UserRole.ADMIN) ? ADMIN_MIN_LENGTH : CUSTOMER_MIN_LENGTH;
        return password.length() >= minLength;
    }

    @Override
    public boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() &&
                Pattern.compile("^[A-Za-z\\s]+$").matcher(name).matches();
    }
}