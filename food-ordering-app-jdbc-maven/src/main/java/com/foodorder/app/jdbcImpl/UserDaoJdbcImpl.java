package com.foodorder.app.jdbcImpl;

import com.foodorder.app.dao.UserDao;
import com.foodorder.app.entities.User;
import com.foodorder.app.enums.UserRole;
import com.foodorder.app.exceptions.FailedToPerformOperation;
import com.foodorder.app.exceptions.ValueAlreadyExistsException;
import com.foodorder.app.utility.ConnectionUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoJdbcImpl implements UserDao {
    private static final UserDaoJdbcImpl userDaoJdbc = new UserDaoJdbcImpl();
    private Connection con;

    private UserDaoJdbcImpl() {
        initSqlDataConnection();
    }

    public static UserDaoJdbcImpl getUserDaoJdbcImpl() {
        return userDaoJdbc;
    }

    void initSqlDataConnection() {
        this.con = ConnectionUtility.getConnection();
    }

    @Override
    public void addUser(User user) throws ValueAlreadyExistsException, FailedToPerformOperation, SQLException {
        String insertData = "INSERT INTO users (name,email,password,address,role_type) VALUES (?,?,?,?,?)";

        PreparedStatement ps = con.prepareStatement(insertData);
        ps.setString(1, user.getName());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getAddress());
        ps.setString(5, user.getRole().name());

        int i = ps.executeUpdate();
        if (i >= 1) {
            System.out.println("Data persisted");
        } else {
            System.out.println("Data not persisted");
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) throws SQLException {
        String query = "SELECT user_id, name, email, password, address, role_type, is_logged_in FROM users WHERE email = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = User.builder()
                            .userId(rs.getLong("user_id"))
                            .name(rs.getString("name"))
                            .email(rs.getString("email"))
                            .password(rs.getString("password"))
                            .address(rs.getString("address"))
                            .role(UserRole.valueOf(rs.getString("role_type")))
                            .isLoggedIn(rs.getBoolean("is_logged_in"))
                            .build();

                    return Optional.of(user);
                }
            }
        }
        return Optional.empty();
    }


    @Override
    public List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("user_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("address"),
                        UserRole.valueOf(resultSet.getString("role_type")),
                        resultSet.getBoolean("is_logged_in")
                );
                userList.add(user);
            }
        }
        return userList;
    }

    @Override
    public boolean setLoginStatus(String email) throws SQLException {
        Optional<User> userByEmail = getUserByEmail(email);
        if (userByEmail.isPresent()) {
            String query = "UPDATE users SET is_logged_in = ? WHERE email = ?";

            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setBoolean(1, true);
                ps.setString(2, email);

                int i = ps.executeUpdate();
                return i > 0;
            }
        }
        return false;
    }

    @Override
    public boolean logoutUser(String email) throws SQLException {
        Optional<User> userByEmail = getUserByEmail(email);
        if (userByEmail.isPresent()) {
            String query = "UPDATE users SET is_logged_in = ? WHERE email = ?";

            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setBoolean(1, false);
                ps.setString(2, email);

                int i = ps.executeUpdate();
                return i > 0;
            }
        }
        return false;
    }

//    @Override
//    public boolean updateUser(User user) {
//        return false;
//    }
//
//    @Override
//    public Optional<User> deleteUser(User user) {
//        return Optional.empty();
//    }
}
