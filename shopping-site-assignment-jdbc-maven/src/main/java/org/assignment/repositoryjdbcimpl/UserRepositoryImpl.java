package org.assignment.repositoryjdbcimpl;

import org.assignment.entities.User;
import org.assignment.enums.Roles;
import org.assignment.queries.CustomerQueries;
import org.assignment.repository.interfaces.UserRepository;
import org.assignment.util.ColorCodes;
import org.assignment.util.ConnectionUtility;

import java.sql.*;
import java.util.*;

import  org.assignment.util.ResultSetUtility;

public class UserRepositoryImpl implements UserRepository {
    private Map<Long, User> customerMap;
    private Connection con;

    public UserRepositoryImpl() {
        initCustomRepo();
    }

    private void initCustomRepo(){
        this.con = ConnectionUtility.getConnection();
    }
    @Override
    public List<User> getCustomers() throws SQLException {
        PreparedStatement statement = con.prepareStatement(CustomerQueries.getAllCustomerQuery());
        return ResultSetUtility.getCustomersFromResultSet(statement.executeQuery());
    }

    @Override
    public Optional<User> fetchById(Long id) throws SQLException {
        String columns[] = {"CUSTOMER_ID"};
        String query = CustomerQueries.getCustomerOrAdminByColumn(columns, null);
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, id);
        List<User> user =  ResultSetUtility.getCustomersFromResultSet(statement.executeQuery());
        return user.isEmpty() ? Optional.empty() : Optional.of(user.get(0));
    }

    @Override
    public Optional<User> fetchAdminById(Long id) throws SQLException {
        String columns[] = {"CUSTOMER_ID", "ROLE"};
        String query = CustomerQueries.getCustomerOrAdminByColumn(columns, "AND");
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, id);
        statement.setObject(2, Roles.ADMIN, Types.OTHER);
        List<User> user =  ResultSetUtility.getCustomersFromResultSet(statement.executeQuery());
        return user.isEmpty() ? Optional.empty() : Optional.of(user.get(0));
    }

    @Override
    public Optional<User> fetchByEmail(String email) throws SQLException {
        String columns[] = {"EMAIL"};
        String query = CustomerQueries.getCustomerOrAdminByColumn(columns, null);
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, email);
        List<User> user =  ResultSetUtility.getCustomersFromResultSet(statement.executeQuery());
        return user.isEmpty() ? Optional.empty() : Optional.of(user.get(0));
    }

    @Override
    public User addCustomer(User user) throws SQLException {
        String columns[] = {"EMAIL", "PASSWORD", "NAME", "ADDRESS", "ROLE"};
        String query =CustomerQueries.addCustomerQuery(columns);
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getName());
        statement.setString(4, user.getAddress());
        statement.setObject(5, Roles.CUSTOMER, Types.OTHER);
        statement.executeUpdate();
        return user;
    }

    @Override
    public User updateCustomer(User user) throws SQLException {
        String columns[] = {"EMAIL", "PASSWORD", "NAME", "ADDRESS", "ROLE"};
        String customerIdColumn = "CUSTOMER_ID";
        String query = CustomerQueries.updateCustomerQuery(columns, customerIdColumn);
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getAddress());
            statement.setObject(5, user.getRole(), Types.OTHER);
            statement.setLong(6, user.getId());
            statement.executeUpdate();
            return user;
    }

    @Override
    public void removeCustomer(User user) {
        String customerColumnName = "CUSTOMER_ID";
String query = CustomerQueries.deleteCustomerQuery(customerColumnName);
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setLong(1, user.getId());
            statement.executeUpdate();
            customerMap.remove(user.getId());
        } catch (SQLException e) {
            System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
        }
    }

    @Override
    public Optional<User> fetchSellerById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<User> fetchAllSellers() {
        return List.of();
    }
}
