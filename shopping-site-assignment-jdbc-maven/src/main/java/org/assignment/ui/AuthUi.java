package org.assignment.ui;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assignment.entities.User;
import org.assignment.enums.ResponseStatus;

import org.assignment.services.AuthenticationService;
import org.assignment.services.UserService;
import org.assignment.services.ProductService;
import org.assignment.util.ColorCodes;
import org.assignment.util.Constants;
import org.assignment.util.FormValidation;

import org.assignment.util.Response;
import org.assignment.wrappers.ProductWrapper;
import org.hibernate.HibernateException;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@AllArgsConstructor
@Slf4j
public class AuthUi extends UI {
    private final UserService userService;
    private final Scanner sc = new Scanner(System.in);
    private final AuthenticationService service;
    private final ProductService productService;

    @Override
    public void initAdminServices(User admin) {
    }

    @Override
    public void initAuthServices() {

        try {
            System.out.println(ColorCodes.GREEN + "***********WELCOME*************" + ColorCodes.RESET);
            Response response = productService.getAllProduct();
            if (response.getStatus() == ResponseStatus.SUCCESSFUL) {
                printProducts((List<ProductWrapper>) response.getData());
            } else {
                System.out.println("Encountered some error... please contact admin");
            }
        } catch (HibernateException e) {
            System.out.println("Encountered some error... please contact admin");
            log.error("Some error occured while starting application ", e);
            return;
        }

        String operation = "";
        while (!operation.equalsIgnoreCase("0")) {
            super.displayOptions(List.of("Press 0 for Log in.", "Press 1 for Registration","Press 2 to exit program", "Operation : "));

            operation = sc.nextLine();
            try {
                Response response = new Response(ResponseStatus.ERROR, null, "No Inputs");
                if (operation.equalsIgnoreCase("0")) {
                    response = login();
                } else if (operation.equalsIgnoreCase("1")) {
                    response = register();
                } else if ( operation.equalsIgnoreCase("2")) {
                   break;
                }
                printResponse(response);

            } catch (Exception e) {
                log.error("Error occured while performing authentication operation", e);
                System.out.println(ColorCodes.RED + Constants.ERROR_MESSAGE + ColorCodes.RESET);
            }
        }
    }

    @Override
    public void initCustomerServices(User user) {}

    private Response login() {
        System.out.println(ColorCodes.GREEN + "*************LOG-IN*****************" + ColorCodes.RESET);
        System.out.print("Enter Email : ");
        String email = sc.nextLine().toUpperCase();
        System.out.print("Enter Password : ");
        String password = sc.nextLine();
        Response authResponse = service.login(email, password);
        while ((!email.isBlank() && !password.isBlank()) && authResponse.getStatus() == ResponseStatus.ERROR) {
            printResponse(authResponse);
            System.out.print("Enter email : ");
            email = sc.nextLine().toUpperCase();
            System.out.print("Enter password : ");
            password = sc.nextLine();
            authResponse = service.login(email, password);
        }

        return authResponse;
    }

    private Response register() {
        Response response;
        System.out.println(ColorCodes.GREEN + "*******REGISTRATION*******" + ColorCodes.RESET);
        System.out.print("Your Name : ");
        String name = sc.nextLine();
        System.out.print("Your Address : ");
        String address = sc.nextLine();
        boolean validEmail = false;
        String email = "";
        while (!validEmail) {
            System.out.print("Your Email : ");
            email = sc.nextLine();
            validEmail = FormValidation.validateEmail(email);
            if (!validEmail) {
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
            if (!validPassword) {
                System.out.println("The password must follow the provided instruction");
                printPasswordDisclaimer();
            }
        }
        email = email.toUpperCase();
        response = service.save(email, password, address, name);
        return response.getStatus() == ResponseStatus.SUCCESSFUL ? login() : response;
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
}
