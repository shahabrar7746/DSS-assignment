package org.assignment.repositoryjdbcimpl;

import org.assignment.entities.Customer;
import org.assignment.enums.Roles;
import org.assignment.queries.CustomerQueries;
import org.assignment.repository.interfaces.CustomerRepository;
import org.assignment.util.ColorCodes;
import org.assignment.util.ConnectionUtility;

import java.sql.*;
import java.util.*;

import  org.assignment.util.ResultSetUtility;

public class CustomerRepositoryImpl implements CustomerRepository {
    private Map<Long, Customer> customerMap;
    private Connection con;

    public CustomerRepositoryImpl() {
        initCustomRepo();
    }

    private void initCustomRepo(){
        this.con = ConnectionUtility.getConnection();
    }
    @Override
    public List<Customer> getCustomers() throws SQLException {
        PreparedStatement statement = con.prepareStatement(CustomerQueries.getAllCustomerQuery());
        return ResultSetUtility.getCustomersFromResultSet(statement.executeQuery());
    }

    @Override
    public Optional<Customer> fetchById(Long id) throws SQLException {
        String columns[] = {"CUSTOMER_ID"};
        String query = CustomerQueries.getCustomerOrAdminByColumn(columns, null);
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, id);
        List<Customer> customer =  ResultSetUtility.getCustomersFromResultSet(statement.executeQuery());
        return customer.isEmpty() ? Optional.empty() : Optional.of(customer.get(0));
    }

    @Override
    public Optional<Customer> fetchAdminById(Long id) throws SQLException {
        String columns[] = {"CUSTOMER_ID", "ROLE"};
        String query = CustomerQueries.getCustomerOrAdminByColumn(columns, "AND");
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, id);
        statement.setObject(2, Roles.ADMIN, Types.OTHER);
        List<Customer> customer =  ResultSetUtility.getCustomersFromResultSet(statement.executeQuery());
        return customer.isEmpty() ? Optional.empty() : Optional.of(customer.get(0));
    }

    @Override
    public Optional<Customer> fetchByEmail(String email) throws SQLException {
        String columns[] = {"EMAIL"};
        String query = CustomerQueries.getCustomerOrAdminByColumn(columns, null);
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, email);
        List<Customer> customer =  ResultSetUtility.getCustomersFromResultSet(statement.executeQuery());
        return customer.isEmpty() ? Optional.empty() : Optional.of(customer.get(0));
    }

    @Override
    public Customer addCustomer(Customer customer) throws SQLException {
        String columns[] = {"EMAIL", "PASSWORD", "NAME", "ADDRESS", "ROLE"};
        String query =CustomerQueries.addCustomerQuery(columns);
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, customer.getEmail());
        statement.setString(2, customer.getPassword());
        statement.setString(3, customer.getName());
        statement.setString(4, customer.getAddress());
        statement.setObject(5, Roles.CUSTOMER, Types.OTHER);
        statement.executeUpdate();
        return customer;
    }

    @Override
    public Customer updateCustomer(Customer customer) throws SQLException {
        String columns[] = {"EMAIL", "PASSWORD", "NAME", "ADDRESS", "ROLE"};
        String customerIdColumn = "CUSTOMER_ID";
        String query = CustomerQueries.updateCustomerQuery(columns, customerIdColumn);
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, customer.getEmail());
            statement.setString(2, customer.getPassword());
            statement.setString(3, customer.getName());
            statement.setString(4, customer.getAddress());
            statement.setObject(5, customer.getRole(), Types.OTHER);
            statement.setLong(6, customer.getId());
            statement.executeUpdate();
            return customer;
    }

    @Override
    public void removeCustomer(Customer customer) {
        String customerColumnName = "CUSTOMER_ID";
String query = CustomerQueries.deleteCustomerQuery(customerColumnName);
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setLong(1, customer.getId());
            statement.executeUpdate();
            customerMap.remove(customer.getId());
        } catch (SQLException e) {
            System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
        }
    }
}
