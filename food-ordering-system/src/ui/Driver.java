package ui;


import utility.ColourCodes;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Driver {
    static Scanner scanner = new Scanner(System.in);
    static AdminUi admin = new AdminUi();

    public static void start() {
        int choice = 0;
        while (choice != 3) {
            try {
                System.out.println(ColourCodes.CYAN + "\nWELCOME TO FOOD ORDERING APPLICATION" + ColourCodes.RESET);
                System.out.println("---------------------------------------");
                System.out.println("Your Logging in as: ");
                System.out.println("1.Admin");
                System.out.println("2.Customer");
                System.out.println("3.Exit");
                System.out.println("Choose an option");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) { // replace with lambda expression
                    case 1:
                        admin.adminMenu(scanner);
                        break;
                    case 2:
                        CustomerUi.customerMenu(scanner);
                        break;
                    case 3:
                        break;
                    default:
                        System.out.println("Invalid choice please try again. ");
                }
            } catch (InputMismatchException e) { // replace with log.error
                System.out.println(ColourCodes.RED + "Please enter correct input.**" + ColourCodes.RED);
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
        System.out.println("Thank you for using food ordering application. ");
    }
}
