package org.assignment.serviceimlementation;

import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import org.assignment.entities.Customer;

import org.assignment.enums.ResponseStatus;
import org.assignment.enums.Roles;

import org.assignment.repository.interfaces.CustomerRepository;

import org.assignment.services.*;

import org.assignment.ui.AdminUI;
import org.assignment.ui.CustomerUI;

import org.assignment.ui.UI;
import org.assignment.util.Constants;
import org.assignment.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;

import java.util.Optional;

@AllArgsConstructor
public class AuthenticationServiceImplementation implements AuthenticationService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private CustomerRepository customerRepository;
    private CustomerService customerService;
    private AdminService adminService;
    private ProductService productService;
    private final OrderService orderService;
    private final CartService cartService;

    @Override
    public Response login(String email, String pasword) {
        Response response = null;
        try {
            Optional<Customer> customer = customerRepository.fetchByEmail(email);
            if (customer.isPresent()) {
                Customer c = customer.get();
                if (c.getRole() == Roles.ADMIN || c.getRole() == Roles.SUPER_ADMIN) {
                    UI adminUI = new AdminUI(adminService, customerService, productService, orderService);
                    adminUI.initAdminServices(c);
                } else if (c.getRole() == Roles.CUSTOMER) {
                    UI customerUI = new CustomerUI(orderService, productService, cartService);
                    customerUI.initCustomerServices(c);
                }
                response = new Response("Logging out");
            }
        } catch (SQLException e) {
            log.error("Some error occured while log in for email {} and password {} ", email, pasword, e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return response;
    }

    /**
     * Registers user using provided params if user already exist in main.repository returns false.
     *
     * @return true on successful log in and false if the credentials already exists in main.repository.
     */
    @Override
    public Response save(String email, String password, String address, String name) {
        Response response = null;
        try {
            Optional<Customer> optionalCustomer = customerRepository.fetchByEmail(email);
            if (optionalCustomer.isPresent()) {
                response = new Response(null, "User already exists");
            }
            if (response == null) {
                Customer newCustomer = Customer.builder()
                        .role(Roles.CUSTOMER)
                        .address(address)
                        .email(email)
                        .name(name)
                        .password(password)
                        .registeredOn(LocalDateTime.now())
                        .build();
                customerRepository.addCustomer(newCustomer);
                response = new Response(newCustomer);
            }
        } catch (SQLException | PersistenceException e) {
            log.error("Some error occured while registering customer with email {} and password {} ", email, password, e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return response;
    }

}
