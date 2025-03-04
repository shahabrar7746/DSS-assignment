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
    public void registerUser(User user) {
        userDao.addUser(user);
    }

    @Override
    public Response<User> loginUser(String email, String password) {
        User user = userDao.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)){
            return new Response<>(user, ResponseStatus.SUCCESS);
        }
        return new Response<>(ResponseStatus.FAILURE);
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
}