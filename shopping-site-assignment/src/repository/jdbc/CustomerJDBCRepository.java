package repository.jdbc;

import entities.Customer;
import enums.Roles;
import repository.interfaces.CustomerRepository;
import util.ColorCodes;
import util.ConnectionUtility;
import util.CustomerUtility;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CustomerJDBCRepository implements CustomerRepository {
    private Map<Long, Customer> customerMap;
    private final Connection con;

    public CustomerJDBCRepository() throws SQLException {

        this.con = ConnectionUtility.getConnection("jdbc:postgresql://172.16.1.195:5331/dbhdemo", "dbhuser", "Fy2aWdXt");
        populateMap();
    }

    @Override
    public List<Customer> getCustomers() {
        return new ArrayList<>( customerMap.values());
    }

    @Override
    public Optional<Customer> fetchById(Long id) {

      return  customerMap.containsKey(id) ? Optional.of(customerMap.get(id)) : Optional.empty();
    }

    @Override
    public Optional<Customer> fetchAdminById(Long id) {
        List<Customer> customers = new ArrayList<>( customerMap.values());
        return customers.stream().filter(c -> {
            return c.getRole() == Roles.ADMIN && c.getId().equals(id);
        }).findAny();
    }

    @Override
    public Optional<Customer> fetchByEmail(String email) {
        List<Customer> customerList =  new ArrayList<>( customerMap.values());
        Map<String, Customer> emailMap = customerList.stream().collect(Collectors.toConcurrentMap(c-> c.getEmail() , c -> c));
        return emailMap.containsKey(email) ? Optional.of(emailMap.get(email)) : Optional.empty();
    }

    @Override
    public void addCustomer(Customer customer) {
        try {
            addCustomerHelper(customer);
        } catch (SQLException e) {
            System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
        }
        populateMap();
    }
    private void addCustomerHelper(Customer customer) throws SQLException {
        String query = "INSERT INTO CUSTOMER(EMAIL, PASSWORD, NAME, ADDRESS, ROLE) " +
                "VALUES(?,?, ?, ?, ?)";

        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, customer.getEmail());
        statement.setString(2, customer.getPassword());
        statement.setString(3, customer.getName());
        statement.setString(4, customer.getAddress());
        statement.setObject(5, Roles.CUSTOMER, Types.OTHER);
        statement.executeUpdate();
    }

    @Override
    public void updateCustomer(Customer customer) {
       updateCustomer(customer, customer.getId());
    }

    @Override
    public void removeCustomer(Customer customer) {
String query = "DELETE FROM CUSTOMER WHERE CUSTOMER_ID = ?;";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setLong(1, customer.getId());
            statement.executeUpdate();
            customerMap.remove(customer.getId());
        } catch (SQLException e) {
            System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
        }
    }

    private void updateCustomer(Customer customer, Long id){
        String query = "UPDATE CUSTOMER SET EMAIL = ?, PASSWORD = ?, NAME = ?, ADDRESS = ?, ROLE = ?  WHERE CUSTOMER_ID = ?";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, customer.getEmail());
            statement.setString(2, customer.getPassword());
            statement.setString(3, customer.getName());
            statement.setString(4, customer.getAddress());
            statement.setObject(5, customer.getRole(), Types.OTHER);
            statement.setLong(6, id);
            statement.executeUpdate();
            maintainMap(customer);
        } catch (SQLException e) {
            System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
        }
    }
    private void maintainMap(Customer customer){
        Customer oldCustomer = customerMap.get(customer.getId());
        customerMap.replace(customer.getId(), oldCustomer, customer);
    }
    private static Map<Long, Customer> getCustomersFromResultSet(ResultSet set) throws SQLException {
        Map<Long, Customer> map = new ConcurrentHashMap<>();

        while(set.next()){
            Long id = set.getLong("customer_id");
            String email = set.getString("email");
            String password = set.getString("password");
            String name = set.getString("name");
            String address = set.getString("address");
            Roles roles  = Roles.valueOf( set.getString("role"));
            Timestamp timestamp = set.getTimestamp("registered_on");
            map.put(id, new Customer(id,name,email, password, address, timestamp, roles));
        }
set.close();
        return map;
    }

    private void populateMap() {
        String query = "SELECT * FROM CUSTOMER";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            customerMap = getCustomersFromResultSet(statement.executeQuery());
        } catch (Exception e) {
            System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
        }
    }

    public static void main(String[] args) throws SQLException {
        CustomerRepository customerRepository = new CustomerJDBCRepository();
        System.out.println(customerRepository.getCustomers());
    }
}
