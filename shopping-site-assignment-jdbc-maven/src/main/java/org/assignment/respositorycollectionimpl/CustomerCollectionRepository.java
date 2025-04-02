package org.assignment.respositorycollectionimpl;

import org.assignment.entities.Customer;

import org.assignment.enums.Roles;
import org.assignment.exceptions.CustomerNotFoundException;
import org.assignment.repository.interfaces.CustomerRepository;

import java.time.LocalDateTime;
import java.util.*;

import java.util.stream.Collectors;

/**
 * Replicates functionality of database.
 */
public class CustomerCollectionRepository implements CustomerRepository {
    private List<Customer> customers;


    @Override
    public List<Customer> getCustomers()  {
        return customers;
    }

    /*
     * Used to populate List of customers.
     */
    public CustomerCollectionRepository() {
        init();
    }

    private void init() {
        this.customers = new ArrayList<>();
        addCustomer("Abrar", "superAdmin@gmail.com", "mumbai", System.getenv("SUPER_ADMIN_PASSWORD"), Roles.SUPER_ADMIN);//change to super admin after final review
        addCustomer("sam", "admin@gmail.com", "ambernath", "admin", Roles.ADMIN);
        addCustomer("joe", "customer@gmail.com", "ambernath", "customer", Roles.CUSTOMER);
    }


    private void addCustomer(String name, String email, String address, String password, Roles role) {
        long id = new Random().nextLong(0, 9000);
        Customer customer = Customer.builder()
                .email(email)
                .name(name)
                .address(address)
                .password(password)
                .registeredOn(LocalDateTime.now())
                .id(id)
                .role(Roles.CUSTOMER)
                .build();
        customers.add(customer);
    }


    @Override
    public Optional<Customer> fetchById(Long id) {
        Map<Long, Customer> map = customers.stream().collect(Collectors.toConcurrentMap(Customer::getId, c -> c));

        Optional<Customer> customer = map.containsKey(id) && map.get(id).getRole() == Roles.CUSTOMER ? Optional.of(map.get(id)) : Optional.empty();

        return customer;
    }

    /**
     * Searches for admin based on the id.
     *
     * @param id id to be searched.
     * @return returns Optional of customer if any admin with the same id is found or else false.
     */
    @Override
    public Optional<Customer> fetchAdminById(Long id) {
        Map<Long, Customer> map = customers.stream().collect(Collectors.toConcurrentMap(Customer::getId, c -> c));
        return map.containsKey(id) && map.get(id).getRole() == Roles.ADMIN ? Optional.of(map.get(id)) : Optional.empty();
    }


    @Override
    public Optional<Customer> fetchByEmail(String email) {
        Map<String, Customer> map = null;
        boolean isNull = true;
        try {
            map = customers.stream().collect(Collectors.toConcurrentMap(Customer::getEmail, c -> c));
            isNull = false;
        } catch (IllegalStateException e) {
            System.err.println("Duplicate entry found");
        }
        if (isNull) {
            return Optional.empty();
        }
        return map.containsKey(email) ? Optional.of(map.get(email)) : Optional.empty();
    }

    @Override
    public Customer addCustomer(Customer customer) {

        addCustomer(customer.getName(), customer.getEmail(), customer.getAddress(), customer.getPassword(), customer.getRole());
        return customer;
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        customers.remove(customer);
        customers.add(customer);
        return customer;
    }

    @Override
    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }
}
