package serviceImpl;

import dao.UserDao;
import entities.User;
import enums.ResponseStatus;
import exceptions.FailedToPerformOperation;
import exceptions.ValueAlreadyExistsException;
import service.UserService;
import utility.Response;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    Response response;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Response getAllUsers() {
        List<User> allUsers = userDao.getAllUsers();

        if (allUsers.isEmpty()) {
            response = new Response(ResponseStatus.FAILURE, "No users found.");
        } else {
            response = new Response(allUsers, ResponseStatus.SUCCESS, "Users fetched successfully");
        }
        return response;
    }


    @Override
    public Response setLoginStatus(String email) {
        Optional<User> optionalUser = userDao.getUserByEmail(email);

        return optionalUser.map(user -> {
            user.setLoggedIn(true);
            return new Response(Boolean.TRUE, ResponseStatus.SUCCESS, "User successfully logged In.");
        }).orElseGet(() -> new Response(ResponseStatus.FAILURE, "Fail to update the status. "));
    }

    @Override
    public Response logoutUser(String email) {
        Optional<User> optionalUser = userDao.getUserByEmail(email);

        return optionalUser.map(user -> {
            user.setLoggedIn(false);
            return new Response(user, ResponseStatus.SUCCESS, "Logged out successfully.");
        }).orElseGet(() -> new Response(ResponseStatus.FAILURE, "unable to log out: User not found."));
    }
}