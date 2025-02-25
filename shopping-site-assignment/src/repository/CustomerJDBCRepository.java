package repository;

import entities.Customer;
import enums.Roles;
import repository.interfaces.CustomerRepository;
import util.ColorCodes;
import util.CustomerUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CustomerJDBCRepository implements CustomerRepository {
    private Map<Long, Customer> customerMap;
    private final Connection con;

    public CustomerJDBCRepository() throws SQLException {
        populateMap();
        this.con = CustomerUtility.getConnection("jdbc:postgresql://172.16.1.195:5331/dbhdemo", "dbhuser", "Fy2aWdXt");
        this.customerMap = new ConcurrentHashMap<>();
    }

    @Override
    public List<Customer> getCustomerMap() {
        return (List<Customer>) customerMap.values();
    }

    @Override
    public Optional<Customer> fetchById(Long id) {

      return  customerMap.containsKey(id) ? Optional.of(customerMap.get(id)) : Optional.empty();
    }

    @Override
    public Optional<Customer> fetchAdminById(Long id) {
        List<Customer> customers = (List<Customer>) customerMap.values();
        return customers.stream().filter(c -> {
            return c.getRole() == Roles.ADMIN && c.getId().equals(id);
        }).findAny();
    }

    @Override
    public Optional<Customer> fetchByEmail(String email) {
        List<Customer> customerList = (List<Customer>) customerMap.values();
        Map<String, Customer> emailMap = customerList.stream().collect(Collectors.toConcurrentMap(c-> c.getEmail() , c -> c));
        return emailMap.containsKey(email) ? Optional.of(emailMap.get(email)) : Optional.empty();
    }

    @Override
    public void addCustomer(Customer customer) {
        //to be implemented.

        populateMap();
    }

    private void populateMap() {
        String query = "SELECT * FROM CUSTOMER where role = 'CUSTOMER'";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            customerMap = CustomerUtility.getCustomersFromResultSet(statement.executeQuery());
        } catch (Exception e) {
            System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
        }
    }

    public static void main(String[] args) throws SQLException {
        CustomerRepository customerRepository = new CustomerJDBCRepository();
        System.out.println(customerRepository.getCustomerMap());
    }
}
