package org.assignment;

import org.assignment.exceptions.NoProductFoundException;
import org.assignment.repository.interfaces.ProductRepository;

import org.assignment.repositoryjdbcimpl.ProductRepositoryImpl;

import org.assignment.services.AuthenticationService;
import org.assignment.ui.CustomerUI;
import org.assignment.ui.UI;
import org.assignment.util.ColorCodes;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Driver {
 //  private static  final Logger logger = LoggerFactory.getLogger(Driver.class);
    public static void main(String[] args) throws SQLException, NoProductFoundException {

       // logger.info("this is for test");
        final ProductRepository productRepository = new ProductRepositoryImpl();
        final  AuthenticationService auth = AuthenticationService.getInstance();
        System.out.println(ColorCodes.GREEN + "***********WELCOME*************" + ColorCodes.RESET);
        System.out.println(ColorCodes.BLUE + "Products : " + productRepository.fetchProducts() + ColorCodes.RESET);
        String operation = "";
        UI ui = new CustomerUI();
        ui.displayOptions(List.of("Press 1 for log in.", "Press 2 for registration","Operation : " ));
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
