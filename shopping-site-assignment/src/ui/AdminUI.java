package ui;

import entities.Customer;
import enums.Roles;

import exceptions.UnauthorizedOperationException;

import services.AdminService;

import serviceimlementation.AdminServiceImplementation;

import util.ColorCodes;

import java.util.*;

public class AdminUI implements UserInterface{

    private final Scanner sc;
    private final AdminService service;

    public AdminUI() {
        this.service = AdminServiceImplementation.getInstance();
        this.sc = new Scanner(System.in);
    }


    @Override
    public void init(Customer admin) {
        boolean isSuperAdmin = Objects.equals(admin.getRole(), Roles.SUPER_ADMIN);
        if (isSuperAdmin) {
            System.out.println(ColorCodes.GREEN + "*********WELCOME-SUPER-ADMIN****************" + ColorCodes.RESET);
        } else {
            System.out.println(ColorCodes.GREEN + "*********WELCOME-ADMIN****************" + ColorCodes.RESET);
        }
        String operation = "";
        while (!operation.equalsIgnoreCase("back")) {
            final String response = "Response : ";
            System.out.println("Press 1 to get all the customer");
            boolean isExit = false;
            System.out.println("Press 2 to get all products");
            System.out.println("Press 3 to get customer by id");
            System.out.println("Press 4 to get customers whose products has been delivered");
            System.out.println("Press 5 to get the products by their type");
            System.out.println("Press 6 to fetch all admins");
            if (isSuperAdmin) {
                System.out.println("Press 7 to cancel a order");
                System.out.println("Press 8 to delete customer");
                System.out.println("Press 9 to grant admin access to customer");
                System.out.println("Press 10 to revoke access from customer");
            }

            System.out.println("Enter 'back' to go to previous page");
            operation = sc.nextLine();

            try {
            switch (operation) {

                    case "1":
                        System.out.println(ColorCodes.BLUE + response + service.getAllCustomer() + ColorCodes.RESET);
                        break;
                    case "2":
                        System.out.println(ColorCodes.BLUE + response + service.getAllProdcuts() + ColorCodes.RESET);
                        break;
                    case "3":
                        System.out.println(ColorCodes.BLUE + response + service.getCustomerById() + ColorCodes.RESET);
                        break;
                    case "4":
                        System.out.println(ColorCodes.BLUE + response + service.getAllDeliveredOrders() + ColorCodes.RESET);
                        break;
                    case "5":
                        System.out.println(ColorCodes.BLUE + response + service.getProductsByType() + ColorCodes.RESET);
                        break;
                    case "6":
                        System.out.println(ColorCodes.BLUE + response + service.fetchAllAdmins() + ColorCodes.RESET);
                        break;
                case "7":
                        authorize(isSuperAdmin);
                        service.cancelOrder(isSuperAdmin);
                        break;
                    case "8":
                        authorize(isSuperAdmin);
                        service.deleteCustomer(isSuperAdmin);
                        break;
                    case "9":
                        authorize(isSuperAdmin);
                        service.grantAccess(isSuperAdmin);
                        break;
                    case "10":
                        authorize(isSuperAdmin);
                        service.revokeAccess(isSuperAdmin);
                        break;
                    case "back", "BACK":
                        isExit = true;
                        break;
                    default:
                        System.err.println("Unsupported operation");

            }
            }catch (Exception e){
                System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
            }
            if (isExit) {
                break;
            }
        }
    }

    /**
     * Used to check if the thread has authority to access below methods.
     * @param isSuperAdmin used to indicate whether the user can access the requested service or not.
     * @throws UnauthorizedOperationException if the user does not have permission to access the service.
     */
    private void authorize(boolean isSuperAdmin){
        if (!isSuperAdmin) {
            throw new UnauthorizedOperationException("Your are not authorized to access this service");
        }
    }
}