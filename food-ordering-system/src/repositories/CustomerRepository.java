package repositories;

import entities.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {
    public static List<Customer> customers = new ArrayList<>();

    private CustomerRepository() {
    }

    static {
        customers = new ArrayList<>();
        Customer customer1 = new Customer("cc", "cc", "cc");
        customers.add(customer1);
    }

    public static void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public static List<Customer> getCustomers() {
        return customers;
    }
}