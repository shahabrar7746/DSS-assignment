package org.assignment.ui;

import org.assignment.entities.Customer;
import org.assignment.enums.ResponseStatus;

import org.assignment.services.AuthenticationService;
import org.assignment.services.CustomerService;
import org.assignment.util.ColorCodes;
import org.assignment.util.FormValidation;

import org.assignment.util.Response;

import java.util.Optional;
import java.util.Scanner;

public class AuthUi extends UI {
    private CustomerService customerService;
    private Scanner sc = new Scanner(System.in);
    private  AuthenticationService service;

    public AuthUi(CustomerService customerService, AuthenticationService service) {
        this.customerService = customerService;
        this.service = service;
    }

    @Override
    public void initAuthServices() {

    }
    public Response login() {
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
        return response != null ? response : service.login(email, password);
    }

    public Response register() {
        Response response = null;
        System.out.println(ColorCodes.GREEN + "*******REGISTRATION*******" + ColorCodes.RESET);
        System.out.print("Your name : ");
        String name = sc.nextLine();
        System.out.print("Your Address : ");
        String address = sc.nextLine();
        boolean validEmail = false;
        String email = "";
        while (!validEmail) {
            System.out.print("Your email : ");
            email = sc.nextLine();
            validEmail = FormValidation.validateEmail(email);
            if (! validEmail) {
                System.out.println("The email must follow the provided instruction");
                printEmailDisclaimer();
            }
        }
        boolean validPassword = false;
        String password = "";
        while (!validPassword) {
            System.out.print("Your Password : ");
            password = sc.nextLine();
            validPassword = FormValidation.validatePassword(password);
            if (! validPassword) {
                System.out.println("The password must follow the provided instruction");
                printPasswordDisclaimer();
            }
        }
        response = service.save(email, password , address, name);
        return response.getStatus() == ResponseStatus.SUCCESSFUL ? service.login(email, password) : response;
    }

    private boolean validateLogin(String email, String password) {
        Optional<Customer> optionalCustomer = customerService.findByEmail(email);
        return optionalCustomer.isPresent() ? optionalCustomer.get().getPassword().equals(password) : optionalCustomer.isPresent();
    }

    private void printPasswordDisclaimer() {
        System.out.println(ColorCodes.RED + "Disclaimer : \n" +
                "Password must be at least 8 characters long.\n" +
                "Password must contain at least one uppercase letter.\n" +
                "Password must contain at least one lowercase letter.\n" +
                "Password must contain at least one digit.\n" +
                "Password must contain at least one special character\n" + ColorCodes.RESET);
    }

    private void printEmailDisclaimer() {
        System.out.println(ColorCodes.RED +
                "Disclaimer : \n" +
                        "Email must follow the standard email format: example@domain.com.\n" +
                        "The local part (before the @ symbol) can include letters, digits, underscores (_), plus (+), and hyphen (-).\n" +
                        "The domain part (after the @ symbol) must include letters, digits, and periods (e.g., example.com, example.co.uk).\n" +
                        "The domain extension should be between 2 and 7 characters long (e.g., .com, .net, .org, etc.)." + ColorCodes.RESET);
    }
    private boolean register(String email, String password, String name, String address) {
        Optional<Customer> optionalCustomer = customerService.findByEmail(email);
        if (optionalCustomer.isPresent()) {
            return false;
        }
        service.save(email, password, address, name);
        return true;
    }
}
