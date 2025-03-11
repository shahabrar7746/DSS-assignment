package serviceImpl;

import dao.UserDao;
import entities.User;
import enums.ResponseStatus;
import service.UserService;
import utility.Response;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Response<User> registerUser(User user) {
        Response<Boolean> response = userDao.addUser(user);
        if (response.getResponseStatus().equals(ResponseStatus.SUCCESS)) {
            return new Response<>(user, ResponseStatus.SUCCESS, "User successfully registered ");
        } else {
            return new Response<>(ResponseStatus.FAILURE,"User Not registered ");
        }
    }

    @Override
    public Response<User> loginUser(String email, String password) {
        User user = userDao.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return new Response<>(user, ResponseStatus.SUCCESS, "User successfully validated: Returning from service");
        }
        return new Response<>(ResponseStatus.FAILURE, "User not found: Returning from service");
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public void deleteUser(User user) {
        userDao.deleteUser(user);
    }

    @Override
    public void setLoginStatus(String email) {
        User userByEmail = userDao.getUserByEmail(email);
        userByEmail.setLoggedIn(true);
    }

    @Override
    public void logoutUser(String email) {
        User userByEmail = userDao.getUserByEmail(email);
        userByEmail.setLoggedIn(false);
    }
}