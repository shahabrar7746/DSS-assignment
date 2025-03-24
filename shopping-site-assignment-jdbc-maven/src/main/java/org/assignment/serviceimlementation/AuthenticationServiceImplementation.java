package org.assignment.serviceimlementation;

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
    public Response login() throws TrialLimitExceedException, SQLException {
        Response response = null;
        System.out.println(ColorCodes.GREEN + "*************LOG-IN*****************" + ColorCodes.RESET);
        System.out.print("Enter email : ");
        String email = sc.nextLine();
        System.out.print("Enter password : ");
        String password = sc.nextLine();
        int count = 4;
        while (!validateLogin(email, password) && count-- > 0) {
            System.out.println("Try again");
            System.out.print("Enter email : ");
            email = sc.nextLine();
            System.out.print("Enter password : ");
            password = sc.nextLine();
            if (email.isEmpty() || email.isBlank() || password.isEmpty() || password.isBlank()) {
                continue;
            }

        }
        if (count <= 0) {
            response = new Response(null, "Try limit exceed");
        }
        if (response == null) {
            Optional<Customer> customer = customerRepository.fetchByEmail(email);
            if (customer.isPresent()) {
                Customer c = customer.get();
                try {
                    if (c.getRole() == Roles.ADMIN || c.getRole() == Roles.SUPER_ADMIN) {
                        UI adminUI = new AdminUI();
                        adminUI.initAdminServices(c);
                    } else if (c.getRole() == Roles.CUSTOMER) {
                        UI customerUI = new CustomerUI();
                        customerUI.initCustomerServices(c);
                    }
                    response = new Response("Logging out");
                } catch (SQLException e) {
                    response = LogUtil.logError(e.getLocalizedMessage());
                } catch (UnauthorizedOperationException e) {
                    response = new Response(null, e.getLocalizedMessage());
                }
            }
        }
        return response;
    }

    @Override
    public Response register() throws UserAlreadyExistsException, CustomerNotFoundException, SQLException {
        Response response = null;
        System.out.println(ColorCodes.GREEN + "*******REGISTRATION*******" + ColorCodes.RESET);
        System.out.print("Your name : ");
        String name = sc.nextLine();
        System.out.print("Your Address : ");
        String address = sc.nextLine();
        System.out.print("Your email : ");
        String email = sc.nextLine();
        System.out.print("Your Password : ");
        String password = sc.nextLine();
        final boolean validEmail = FormValidation.validateEmail(email);
        final boolean validPassword = FormValidation.validatePassword(password);
        final boolean credentialValidation = validPassword && validEmail;
        if (credentialValidation && register(email, password, name, address)) {
            Customer customer = login(email, password, true);
            UI customerUi = new CustomerUI();
            try {
                customerUi.initCustomerServices(customer);
                response = new Response("Logging out");
            } catch (UnauthorizedOperationException e) {
                response = new Response(null, e.getLocalizedMessage());
            } catch (SQLException e) {
                LogUtil.logError(e.getLocalizedMessage());
                response = new Response(null, "Some error occured");
            }

        } else if (!validEmail) {
            response = new Response(null,
                    "Disclaimer : \n" +
                            "Email must follow the standard email format: example@domain.com.\n" +
                            "The local part (before the @ symbol) can include letters, digits, underscores (_), plus (+), and hyphen (-).\n" +
                            "The domain part (after the @ symbol) must include letters, digits, and periods (e.g., example.com, example.co.uk).\n" +
                            "The domain extension should be between 2 and 7 characters long (e.g., .com, .net, .org, etc.)."
            );
        } else if (!validPassword) {
            response = new Response(null,
                    "Disclaimer : \n" +
                            "Password must be at least 8 characters long.\n" +
                            "Password must contain at least one uppercase letter.\n" +
                            "Password must contain at least one lowercase letter.\n" +
                            "Password must contain at least one digit.\n" +
                            "Password must contain at least one special character\n"
            );

        } else {
            response = new Response(null, "User already exist");
        }
        return response;
    }

    /**
     * Used to log in System to access main.services.
     *
     * @param email        used to register customer using this email.
     * @param password     used to verify customers authenticity.
     * @param isRedirected indicates if the called after registration.
     * @return Customer object on successful log in, if not found returns null.
     */
    private Customer login(String email, String password, boolean isRedirected) throws CustomerNotFoundException, SQLException {
        if (!isRedirected) {
            System.out.println(ColorCodes.GREEN + "******CUSTOMER LOG IN*******" + ColorCodes.RESET);
        }
        Map<String, Customer> map = customerRepository.getCustomers().stream().collect(Collectors.toConcurrentMap(Customer::getEmail, c -> c));
        return map.containsKey(email) && map.get(email).getPassword().equals(password) ? map.get(email) : null;
    }

    /**
     * Validates log in using provided email and password.
     *
     * @param email    stores user email.
     * @param password stores user password.
     * @return true on valid email and password or else false.
     */
    private boolean validateLogin(String email, String password) throws SQLException {
        Optional<Customer> optionalCustomer = customerRepository.fetchByEmail(email);
        return optionalCustomer.isPresent() ? optionalCustomer.get().getPassword().equals(password) : optionalCustomer.isPresent();
    }

    /**
     * Registers user using provided params if user already exist in main.repository returns false.
     *
     * @return true on successful log in and false if the credentials already exists in main.repository.
     */
    private boolean register(String email, String password, String name, String address) throws SQLException {
        Optional<Customer> optionalCustomer = customerRepository.fetchByEmail(email);
        if (optionalCustomer.isPresent()) {
            return false;
        }
        Customer newCustomer = new Customer(name, email, password, address, LocalDateTime.now(), Roles.CUSTOMER);
        customerRepository.addCustomer(newCustomer);

        return true;
    }
}
