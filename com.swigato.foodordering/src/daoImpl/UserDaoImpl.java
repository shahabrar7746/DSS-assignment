package daoImpl;

import dao.UserDao;
import entities.User;
import exceptions.FailedToPerformOperation;
import exceptions.ValueAlreadyExistsException;

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
    public boolean updateUser(User user) {
        Optional<User> userToUpdate = users.stream().filter(user1 -> user1.getId() == user.getId()).findFirst();

        userToUpdate.ifPresent(u -> {
            u.setName(user.getName());
            u.setEmail(user.getEmail());
            u.setPassword(user.getPassword());
            u.setRole(user.getRole());
            u.setLoggedIn(user.isLoggedIn());
        });
        return userToUpdate.isPresent();
    }

    @Override
    public Optional<User> deleteUser(User user) {
        Optional<User> userToRemove = users.stream()
                .filter(user1 -> user1.getId() == user.getId()).findFirst();

        userToRemove.ifPresent(users::remove);
        return userToRemove;
    }
}
