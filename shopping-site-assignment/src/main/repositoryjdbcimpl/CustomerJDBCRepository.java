package main.repositoryjdbcimpl;

import main.entities.Customer;
import main.enums.Roles;
import main.queries.CustomerQueries;
import main.repository.interfaces.CustomerRepository;
import main.util.ColorCodes;
import main.util.ConnectionUtility;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static main.util.ResultSetUtility.getCustomersFromResultSet;

public class CustomerJDBCRepository implements CustomerRepository {
    private Map<Long, Customer> customerMap;
    private Connection con;

    public CustomerJDBCRepository() {
        initCustomRepo();
    }

    private void initCustomRepo(){
        this.con = ConnectionUtility.getConnection();
    }
    @Override
    public List<Customer> getCustomers() throws SQLException {
        PreparedStatement statement = con.prepareStatement(CustomerQueries.getAllCustomerQuery());
        return getCustomersFromResultSet(statement.executeQuery());
    }

    @Override
    public Optional<Customer> fetchById(Long id) throws SQLException {
        String columns[] = {"CUSTOMER_ID"};
        String query = CustomerQueries.getCustomerOrAdminByColumn(columns, null);
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, id);
        List<Customer> customer =  getCustomersFromResultSet(statement.executeQuery());
        return customer.isEmpty() ? Optional.empty() : Optional.of(customer.get(0));
    }

    @Override
    public Optional<Customer> fetchAdminById(Long id) throws SQLException {
        String columns[] = {"CUSTOMER_ID", "ROLE"};
        String query = CustomerQueries.getCustomerOrAdminByColumn(columns, "AND");
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, id);
        statement.setObject(2, Roles.CUSTOMER, Types.OTHER);
        List<Customer> customer =  getCustomersFromResultSet(statement.executeQuery());
        return customer.isEmpty() ? Optional.empty() : Optional.of(customer.get(0));
    }

    @Override
    public Optional<Customer> fetchByEmail(String email) throws SQLException {
        String columns[] = {"EMAIL"};
        String query = CustomerQueries.getCustomerOrAdminByColumn(columns, null);
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, email);
        List<Customer> customer =  getCustomersFromResultSet(statement.executeQuery());
        return customer.isEmpty() ? Optional.empty() : Optional.of(customer.get(0));
    }

    @Override
    public void addCustomer(Customer customer) throws SQLException {
        String columns[] = {"EMAIL", "PASSWORD", "NAME", "ADDRESS", "ROLE"};
        String query =CustomerQueries.addCustomerQuery(columns);
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, customer.getEmail());
        statement.setString(2, customer.getPassword());
        statement.setString(3, customer.getName());
        statement.setString(4, customer.getAddress());
        statement.setObject(5, Roles.CUSTOMER, Types.OTHER);
        statement.executeUpdate();
    }

    @Override
    public void updateCustomer(Customer customer) throws SQLException {
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
