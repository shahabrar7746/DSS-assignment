package org.assignment.serviceimlementation;

import jakarta.persistence.PersistenceException;
import org.assignment.entities.Customer;

import org.assignment.enums.Roles;

import org.assignment.exceptions.CustomerNotFoundException;
import org.assignment.exceptions.TrialLimitExceedException;
import org.assignment.exceptions.UnauthorizedOperationException;
import org.assignment.exceptions.UserAlreadyExistsException;

import org.assignment.repositoryhibernateimpl.CustomerRepoHibernateImpl;
import org.assignment.repositoryjdbcimpl.CustomerRepositoryImpl;
import org.assignment.repository.interfaces.CustomerRepository;

import org.assignment.services.AuthenticationService;
import org.assignment.services.CustomerService;

import org.assignment.ui.AdminUI;
import org.assignment.ui.CustomerUI;

import org.assignment.ui.UI;
import org.assignment.util.ColorCodes;
import org.assignment.util.FormValidation;
import org.assignment.util.LogUtil;
import org.assignment.util.Response;
import org.hibernate.HibernateException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AuthenticationServiceImplementation implements AuthenticationService {

    private CustomerService customerService;
    private Scanner sc;
    private CustomerRepository customerRepository;
    private static AuthenticationServiceImplementation service;

    public static AuthenticationServiceImplementation getInstance() {
        if (service == null) {
            service = new AuthenticationServiceImplementation();
        }
        return service;
    }

    private AuthenticationServiceImplementation() {
        init();
    }

    public void init() {
        this.customerService = CustomerService.getInstance();
        this.customerRepository = new CustomerRepoHibernateImpl();
        this.sc = new Scanner(System.in);
    }
    @Override
    public Response login(String email, String pasword) {
        Response response = null;
        if (response == null) {
            try {
            Optional<Customer> customer = customerRepository.fetchByEmail(email);
            if (customer.isPresent()) {
                Customer c = customer.get();
                    if (c.getRole() == Roles.ADMIN || c.getRole() == Roles.SUPER_ADMIN) {
                        UI adminUI = new AdminUI();
                        adminUI.initAdminServices(c);
                    } else if (c.getRole() == Roles.CUSTOMER) {
                        UI customerUI = new CustomerUI();
                        customerUI.initCustomerServices(c);
                    }
                    response = new Response("Logging out");
                }
            } catch (SQLException e) {
                response = LogUtil.logError(e.getStackTrace());
            } catch (UnauthorizedOperationException e) {
                response = new Response(null, e.getLocalizedMessage());
            }
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
            if(response == null) {
                Customer newCustomer = new Customer(name, email, password, address, LocalDateTime.now(), Roles.CUSTOMER);
                customerRepository.addCustomer(newCustomer);
                response = new Response(newCustomer);
            }
        }catch (SQLException | PersistenceException e){
            response = LogUtil.logError(e.getStackTrace());
        }
        return response;
    }

}
