package org.assignment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assignment.enums.ResponseStatus;
import org.assignment.exceptions.NoProductFoundException;
import org.assignment.repository.interfaces.ProductRepository;

import org.assignment.repositoryhibernateimpl.ProductRepoHibernateImpl;
import org.assignment.repositoryjdbcimpl.ProductRepositoryImpl;

import org.assignment.services.AuthenticationService;
import org.assignment.ui.AuthUi;
import org.assignment.ui.CustomerUI;
import org.assignment.ui.UI;
import org.assignment.util.ColorCodes;
import org.assignment.util.LogUtil;
import org.assignment.util.Response;
import org.hibernate.HibernateException;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) throws SQLException, NoProductFoundException {
         ProductRepository productRepository = null;
        AuthUi ui = null;
    try {
         productRepository =  ProductRepoHibernateImpl.getInstance();

        System.out.println(ColorCodes.GREEN + "***********WELCOME*************" + ColorCodes.RESET);
        System.out.println(ColorCodes.BLUE + "Products : " + productRepository.fetchProducts() + ColorCodes.RESET);
        ui = new AuthUi();
    }catch (HibernateException e){
        LogUtil.logError(e.getStackTrace());
    }


        String operation = "";

        Scanner sc = new Scanner(System.in);
        while (!operation.equalsIgnoreCase("exit") && productRepository != null) {
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
        if(productRepository == null){
            System.out.println("Encountered some error... please contact admin");
        }
    }
}
