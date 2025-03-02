
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.interfaces.ProductRepository;

import repository.jdbc.ProductJDBCRepository;
import serviceimlementation.AuthenticationServiceImplementation;

import services.AuthenticationService;
import util.ColorCodes;

import java.sql.SQLException;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) throws SQLException {
      Logger logger = LoggerFactory.getLogger(Driver.class);
        logger.info("this is for test");
        final ProductRepository productRepository = new ProductJDBCRepository();
        final  AuthenticationService auth = new AuthenticationServiceImplementation();
        System.out.println(ColorCodes.GREEN + "***********WELCOME*************" + ColorCodes.RESET);
        System.out.println(ColorCodes.BLUE + "Products : " + productRepository.fetchProducts() + ColorCodes.RESET);
        String operation = "";
        while (!operation.equalsIgnoreCase("exit")) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Press 1 for log in.\nPress 2 for registration");
            System.out.print("Operation : ");
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
