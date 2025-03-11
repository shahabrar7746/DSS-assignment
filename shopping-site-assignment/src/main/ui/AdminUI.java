package main.ui;

import main.entities.Customer;
import main.enums.ResponseStatus;
import main.enums.Roles;

import main.exceptions.UnauthorizedOperationException;

import main.services.AdminService;

import main.serviceimlementation.AdminServiceImplementation;

import main.util.ColorCodes;
import main.util.Response;

import java.sql.SQLException;
import java.util.*;

public class AdminUI extends UI {
    private final Scanner sc;
    private final AdminService service;

    public AdminUI() throws SQLException {
        this.service = AdminServiceImplementation.getInstance(); // TODO
        this.sc = new Scanner(System.in);
    }



    public void initAdminServices(Customer admin) {
        boolean isSuperAdmin = Objects.equals(admin.getRole(), Roles.SUPER_ADMIN);
//        String msg = isSuperAdmin ? "akjakjnadna"  : "jhabhdjbadba"; // TODO
//        System.out.println(msg);
        String message = isSuperAdmin ? "SUPER_ADMIN" : "ADMIN";

            System.out.println(ColorCodes.GREEN + "*********WELCOME-"+ message + "****************" + ColorCodes.RESET);

        String operation = "";
        while (!operation.equalsIgnoreCase("back")) {
            final String response = "Response : ";
            List<String> option = new ArrayList<>();
            option.add("Press 1 to get all the customer");
            option.add("Press 2 to get all products");
            option.add("Press 3 to get customer by id");
            option.add("Press 4 to get all delivered orders");
            option.add("Press 5 to get the products by their type");
            option.add("Press 6 to fetch all admins");
            boolean isExit = false;
            if (isSuperAdmin) {
                option.add("Press 7 to cancel a order");
              option.add("Press 8 to delete customer");
             option.add("Press 9 to grant admin access to customer");
                option.add("Press 10 to revoke access from customer");
            }
            option.add("Enter 'back' to go to previous page");
            super.displayOptions(option);
            operation = sc.nextLine();

                Response resp = null;
            switch (operation) {

                    case "1":
                        resp = service.getAllCustomer();
                        break;
                    case "2":
                        resp = service.getAllProdcuts();
                        break;
                    case "3":
                        resp = service.getCustomerById();
                        break;
                    case "4":
                        resp = service.getAllDeliveredOrders();
                        break;
                    case "5":
                        resp = service.getProductsByType();
                        break;
                    case "6":
                     resp = service.fetchAllAdmins();
                        break;
                case "7":
                    resp = service.cancelOrder(isSuperAdmin);
                    break;
                case "8":
                   resp = service.deleteCustomer(isSuperAdmin);
                    break;
                case "9":

                     resp = service.grantAccess(isSuperAdmin);
                        break;
                    case "10":
                        resp = service.revokeAccess(isSuperAdmin);
                        break;
                    case "back", "BACK":
                        isExit = true;
                        break;
                    default:
                        System.err.println();
                        resp = new Response(null, "Unsupported operation");
            }
if(resp.getStatus() == ResponseStatus.ERROR){
    System.out.println(ColorCodes.RED + "ERROR : " + resp.getData() + ColorCodes.RESET);
}else if(resp.getStatus() == ResponseStatus.SUCCESSFUL) {
    System.out.println(ColorCodes.RED + response  + resp.getData() + ColorCodes.RESET);
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
    private void authorize(boolean isSuperAdmin) throws UnauthorizedOperationException {
        if (Boolean.FALSE.equals(isSuperAdmin)) {
            throw new UnauthorizedOperationException("Your are not authorized to access this service");
        }
    }
}