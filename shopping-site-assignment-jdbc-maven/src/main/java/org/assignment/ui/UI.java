package org.assignment.ui;


import org.assignment.entities.Customer;
import org.assignment.exceptions.UnauthorizedOperationException;
import org.assignment.repository.interfaces.ProductRepository;
import org.assignment.repositoryhibernateimpl.ProductRepoHibernateImpl;
import org.assignment.repositoryjdbcimpl.ProductRepositoryImpl;
import org.assignment.services.AuthenticationService;
import org.assignment.util.ColorCodes;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public abstract class UI {

    public void initAdminServices(Customer admin){
    }
    public void initCustomerServices(Customer customer) throws UnauthorizedOperationException, SQLException {}
    public static void displayOptions(List<String> options){
        options.forEach(System.out::println);
    }

    public static void main(String[] args) {
        final ProductRepository productRepository = new ProductRepoHibernateImpl();
        final AuthenticationService auth = AuthenticationService.getInstance();
        System.out.println(ColorCodes.GREEN + "***********WELCOME*************" + ColorCodes.RESET);
        try {
            System.out.println(ColorCodes.BLUE + "Products : " + productRepository.fetchProducts() + ColorCodes.RESET);
        } catch (Exception e) {

        }
        String operation = "";
        displayOptions(List.of("Press 1 for log in.", "Press 2 for registration","Operation : " ));
        while (!operation.equalsIgnoreCase("exit")) {
            Scanner sc = new Scanner(System.in);
            operation = sc.nextLine();
            try {
                if (operation.equalsIgnoreCase("1")) {
                    auth.login();
                } else if (operation.equalsIgnoreCase("2")) {
                    auth.register();
                }
            } catch (Exception e) {
                System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
            }
        }
    }
}
