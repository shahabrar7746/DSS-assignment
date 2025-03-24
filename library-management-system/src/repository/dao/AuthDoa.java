package repository.dao;

import customexception.DataInsertException;
import model.entity.user.User;
import server.interfaces.LoginAndSignUp;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

import static repository.dao.DataBaseImpl.usersDb;


public class AuthDoa implements LoginAndSignUp {
    private static final ReentrantLock lock = new ReentrantLock();
    private User user;
    private String email;
    private String password;

    public AuthDoa(User user) {
        this.user = user;
    }
    public AuthDoa(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public void userRegistration() {
        if (Objects.isNull(usersDb)) {
            usersDb = new HashMap<>();
        }
        if (!usersDb.containsKey(user.getEmail()) && !user.getEmail().equalsIgnoreCase(DataBaseImpl.adminEmail)) {
            //setting the user id
            int idNumber = DataBaseImpl.usersDb.size() + 1;
            String id = "user" + idNumber;
            try {
                user.setId(id.trim());
                user.setPassword((user.getPassword().hashCode() + ("password").hashCode() + "xyz"));
                lock.lock();
                new UserDoa().add(user);
                DataBaseImpl.usersDb.put(user.getEmail(), user);
            } catch (DataInsertException e) {
                System.out.println("something went wrong during signup try again..");
            } finally {
                lock.unlock();
            }
            try {
                LogManager.logManage("\n" + "Name :" + user.getName() + "\n" + "Email :" + user.getEmail() + "\n" + "Password :" + "**********" + "\n" +
                        "Role :" + user.getRole() + "\n" + "Date: " + LocalDate.now() + "\n" + "****************************");
            } catch (IOException e) {
                System.out.println("error during log maintain");
            }
        } else {
            System.out.println("user already exits....");
        }
    }

    @Override
    public User userLogin() {
        if (email != null && password != null) {
            try {
                if (email.equalsIgnoreCase(DataBaseImpl.adminEmail) || usersDb.containsKey(email)) {
                    User user = usersDb.get(email);
                    if (user.getPassword().equals(password.hashCode()+("password").hashCode()+"xyz") || password.equalsIgnoreCase(DataBaseImpl.adminPassword)) {
                        user.setLogin(true);
                        return user;
                    } else {
                        return null;
                    }
                }
            } catch (Exception e) {
                System.out.println("User Not Found");
            }
        }
        return null;
    }
}
