package daoImpl;

import dao.UserDao;
import entities.User;

import java.util.ArrayList;
import java.util.List;

public class RepoUserDao implements UserDao {
    private final List<User> users = new ArrayList<>();

    @Override
    public void addUser(User user) {
        users.add(user);
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