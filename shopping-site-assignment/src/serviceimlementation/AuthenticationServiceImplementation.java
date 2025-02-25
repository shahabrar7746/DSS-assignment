package serviceimlementation;

import entities.Customer;

import enums.Roles;

import exceptions.TrialLimitExceedException;
import exceptions.UserAlreadyExistsException;

import repository.CustomerCollectionRepository;
import repository.jdbc.CustomerJDBCRepository;
import repository.interfaces.CustomerRepository;

import services.AuthenticationService;
import services.CustomerService;

import ui.AdminUI;
import ui.CustomerUI;

import ui.UserInterface;
import util.ColorCodes;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AuthenticationServiceImplementation implements AuthenticationService {

    private CustomerService customerService;
    private final Scanner sc;
private final CustomerRepository customerRepository;
    public AuthenticationServiceImplementation() throws SQLException {
        this.customerService = new CustomerServiceImplementation();
        this.customerRepository = new CustomerJDBCRepository();
        this.sc = new Scanner(System.in);
    }



    @Override
    public void login() {
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

        }
        if (count <= 0) {
            throw new TrialLimitExceedException("Try limit exceed");
        }
        Optional<Customer> customer = customerRepository.fetchByEmail(email);
        customer.ifPresent(c -> {

            if (c.getRole() == Roles.ADMIN || c.getRole() == Roles.SUPER_ADMIN) {
                UserInterface adminUI = null;
                try {
                    adminUI = new AdminUI();
                } catch (SQLException e) {
                    System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
                }
                try {
                    adminUI.init(c);
                } catch (Exception e) {
                    System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
                }
            } else {
                UserInterface customerUI = new CustomerUI();
                customerUI.init(c);
            }
        });
    }



    @Override
    public void register() {
        System.out.println(ColorCodes.GREEN + "*******REGISTRATION*******" + ColorCodes.RESET);
        System.out.print("Your name : ");
        String name = sc.nextLine();
        System.out.print("Your email : ");
        String email = sc.nextLine();
        System.out.print("Your Address : ");
        String address = sc.nextLine();
        System.out.print("Your Password : ");
        String password = sc.nextLine();
        if (register(email, password, name, address)) {
            Customer customer = login(email, password, true);
            customerService.browse(customer);
        } else {
            throw new UserAlreadyExistsException("User already exist");
        }
    }

    /**
     * Used to log in System to access services.
     * @param email used to register customer using this email.
     * @param password used to verify customers authenticity.
     * @param isRedirected indicates if the called after registration.
     * @return Customer object on successful log in, if not found returns null.
     */
    private Customer login(String email, String password, boolean isRedirected) {
        if (!isRedirected) {
            System.out.println(ColorCodes.GREEN + "******CUSTOMER LOG IN*******" + ColorCodes.RESET);
        }
        Map<String, Customer> map = customerRepository.getCustomers().stream().collect(Collectors.toConcurrentMap(Customer::getEmail, c -> c));
        return map.containsKey(email) && map.get(email).getPassword().equals(password) ? map.get(email) : null;
    }

    /**
     * Validates log in using provided email and password.
     * @param email stores user email.
     * @param password stores user password.
     * @return true on valid email and password or else false.
     */
    private  boolean validateLogin(String email, String password) {
        Optional<Customer> optionalCustomer = customerRepository.fetchByEmail(email);
        return optionalCustomer.isPresent() ? optionalCustomer.get().getPassword().equals(password) : optionalCustomer.isPresent();
    }

    /**
     * Registers user using provided params if user already exist in repository returns false.
     *
     * @return true on successful log in and false if the credentials already exists in repository.
     */
    private boolean register(String email, String password, String name, String address) {
        Optional<Customer> optionalCustomer = customerRepository.fetchByEmail(email);
        if (optionalCustomer.isPresent()) {
            return false;
        }
        Customer newCustomer = new Customer(name, email, password, address);
        customerRepository.addCustomer(newCustomer);

        return true;
    }
}
