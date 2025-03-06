package daoImpl;

import dao.UserDao;
import entities.User;
import enums.ResponseStatus;
import enums.UserRole;
import utility.Response;

import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private final List<User> users = new ArrayList<>();

//    public UserDaoImpl() {
//        init();
//    }
//
//    public void init() {
//        User user1 = new User("Chetan", "chetan@gmail.com", "chetan123", UserRole.CUSTOMER);
//        User user2 = new User("Saurav", "s", "s", UserRole.CUSTOMER);
//        User adminUser = new User("admin", "admin@gmai.com", "ds@123", UserRole.ADMIN);
//        User adminUser2 = new User("a", "a", "a", UserRole.ADMIN);
//        addUser(user1);
//        addUser(user2);
//        addUser(adminUser);
//        addUser(adminUser2);
//    }

    @Override
    public Response<Boolean> addUser(User user) {
        boolean success = users.add(user);
        if (success) {
            return new Response<>(Boolean.TRUE, ResponseStatus.SUCCESS);
        }
            return new Response<>(ResponseStatus.FAILURE);
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