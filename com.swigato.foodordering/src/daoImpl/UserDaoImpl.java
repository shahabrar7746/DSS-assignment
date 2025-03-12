package daoImpl;

import dao.UserDao;
import entities.User;
import enums.ResponseStatus;
import utility.Response;

import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private final List<User> users = new ArrayList<>();

    @Override
    public Response<Boolean> addUser(User user) {
        if (users.contains(user)){
            return new Response<>(ResponseStatus.FAILURE, "User already exists: Returning from Dao ");
        }
        boolean success = users.add(user);
        if (success) {
            return new Response<>(Boolean.TRUE, ResponseStatus.SUCCESS, "User found : Returning from Dao");
        }
            return new Response<>(ResponseStatus.FAILURE, "Unable to add user: Returning from Dao ");
    }

    @Override
    public User getUserByEmail(String email) {
        return users.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public void updateUser(User user) {
        users.stream().filter(u -> u.getId() == user.getId()).findFirst().ifPresent(u -> {
            u.setName(user.getName());
            u.setEmail(user.getEmail());
            u.setPassword(user.getPassword());
            u.setRole(user.getRole());
            u.setLoggedIn(user.isLoggedIn());
        });
    }

    @Override
    public void deleteUser(User user) {
        users.removeIf(u -> u.getId() == user.getId());
    }
}