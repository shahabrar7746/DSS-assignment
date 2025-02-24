package ui;

import entities.Admin;
import entities.Order;
import repositories.AdminRepository;
import service.OrderService;
import utility.ColourCodes;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public final class AdminUi {
    private Admin currentAdmin;

    public void adminMenu(Scanner scanner) {
        int choice;
        boolean isExit = true;
        while (isExit) {
            try {
                System.out.println(ColourCodes.CYAN + "\nAdmin Login: \n------------------------" + ColourCodes.RESET);
                System.out.println("1.Login");
                System.out.println("2.Back to main menu");
                System.out.println("Choose an option");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        loginAdmin(scanner);
                        break;
                    case 2:
                        Driver.start();

                    case 3:
                        isExit = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again ");
                }
            } catch (InputMismatchException e) {
                System.out.println(ColourCodes.RED + "Invalid input. Please try again" + ColourCodes.RESET);
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("An error occurred" + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    public void loginAdmin(Scanner scanner) {
        String email = "";
        String password = "";

        if (currentAdmin == null || currentAdmin.isLoggedIn()) {
            System.out.println("Enter email: ");
            email = scanner.nextLine();
            System.out.println("Enter password: ");
            password = scanner.nextLine();
        }
        boolean foundAdmin = false;
        for (Admin admin : AdminRepository.getAdmins()) {
            if (email.equals(admin.getEmail()) && password.equals(admin.getPassword())) {
                admin.setLoggedIn(true);
                System.out.println(ColourCodes.GREEN + "Login successful.." + ColourCodes.RESET);
                currentAdmin = admin;
                foundAdmin = true;
                break;
            }
        }
        if (!foundAdmin) {
            System.out.println("Login failed, Please try again.");
        } else {
            displayAdminOperation(scanner);
        }
    }

    private void displayAdminOperation(Scanner scanner) {
        if (currentAdmin.isLoggedIn()) {
            System.out.println("admin login required.");
        }

        System.out.println(ColourCodes.GREEN + "You logged in ad an admin." + ColourCodes.RESET);
        int choice;
        boolean isExitt = true;
        while (isExitt) {
            System.out.println(ColourCodes.CYAN + "\nADMIN MENU" + ColourCodes.RESET);
            System.out.println("1.Add admin");
            System.out.println("2.Display admins");
            System.out.println("3.Manage food items");
            System.out.println("4.Check customer orders");
            System.out.println("5.Logout");
            System.out.println("6.Return to back menu.");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addAdmin(scanner);
                    break;
                case 2:
                    displayAdmins();
                    break;
                case 3:
                    ManageFoodItemsUi.manageFoodItems(scanner);
                    break;
                case 4:
                    Map<Integer, Order> allOrders = OrderService.getAllOrders();
                    boolean orderStatus = allOrders.isEmpty();
                    System.out.println(orderStatus ? "No orders found" : allOrders + "\n");
                    break;
                case 5:
                    logOutAdmin(scanner);
                    break;
                case 6:
                    isExitt = false;
                    break;
                default:
                    System.out.println(ColourCodes.RED + "Choose correct option " + ColourCodes.RESET);
            }
        }
    }

    private void logOutAdmin(Scanner scanner) {
        System.out.println("Do you really want to logout? (y/n)");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("y")) {
            currentAdmin.setLoggedIn(false);
            currentAdmin = null;
            System.out.println("Logging out..");
            adminMenu(scanner);
        }
        if (confirmation.equalsIgnoreCase("n")) {
            displayAdminOperation(scanner);
        } else {
            System.out.println("Invalid option.");
        }
    }

    public void addAdmin(Scanner scanner) {
        System.out.println("Enter new admin details: \n------------------------");
        System.out.println("Enter name:");
        String name = scanner.nextLine();

        System.out.println("Enter email:");
        String email = scanner.nextLine();

        System.out.println("Set password:");
        String password = scanner.nextLine();

        Admin newAdmin = new Admin(name, email, password);

        boolean success = AdminRepository.addAdmin(newAdmin);
        System.out.println(success ? "Admin added successfully." : "Operation failed.");
    }

    public void displayAdmins() {
        System.out.println("Displaying all the admins: ");
        AdminRepository.getAdmins()
                .forEach(System.out::println);
    }
}