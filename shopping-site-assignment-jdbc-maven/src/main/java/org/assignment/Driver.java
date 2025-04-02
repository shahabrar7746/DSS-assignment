package org.assignment;

import org.assignment.enums.ResponseStatus;
import org.assignment.exceptions.NoProductFoundException;
import org.assignment.repository.interfaces.ProductRepository;

import org.assignment.repositoryhibernateimpl.ProductRepoHibernateImpl;

import org.assignment.services.AuthenticationService;
import org.assignment.services.CustomerService;
import org.assignment.services.ProductService;
import org.assignment.ui.AuthUi;
import org.assignment.util.ColorCodes;
import org.assignment.util.Response;
import org.assignment.wrappers.ProductWrapper;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Driver {
    private final ProductService productService;
    private final AuthenticationService authenticationService;

    public Driver(ProductService productService, AuthenticationService authenticationService, CustomerService customerService) {
        this.productService = productService;
        this.authenticationService = authenticationService;
        this.customerService = customerService;
    }

    private final CustomerService customerService;

    public void start() throws SQLException, NoProductFoundException {
        final Logger log = LoggerFactory.getLogger(this.getClass());
        AuthUi ui = null;
        try {
            System.out.println(ColorCodes.GREEN + "***********WELCOME*************" + ColorCodes.RESET);
            Response response = productService.getAllProduct();
            if (response.getStatus() == ResponseStatus.SUCCESSFUL) {
                System.out.println(ColorCodes.BLUE + "Products : " + (List<ProductWrapper>) response.getData() + ColorCodes.RESET);
            } else {

                System.out.println("Encountered some error... please contact admin");

            }

            ui = new AuthUi(customerService, authenticationService);
        } catch (HibernateException e) {
            log.error("Some error occured while starting application ", e);
        }

        String operation = "";

        Scanner sc = new Scanner(System.in);
        while (!operation.equalsIgnoreCase("exit")) {
            ui.displayOptions(List.of("Press 1 for log in.", "Press 2 for registration", "Operation : "));

            operation = sc.nextLine();
            try {
                Response response = new Response("No Inputs");
                if (operation.equalsIgnoreCase("1")) {
                    response = ui.login();
                } else if (operation.equalsIgnoreCase("2")) {
                    response = ui.register();
                }
                if (response.getStatus().equals(ResponseStatus.SUCCESSFUL)) {
                    System.out.println(ColorCodes.BLUE + response.getData() + ColorCodes.RESET);
                } else {
                    System.out.println(ColorCodes.RED + response.getError() + ColorCodes.RESET);
                }
            } catch (Exception e) {
                System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
                e.printStackTrace();
            }
        }

    }
}
