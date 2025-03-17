package org.assignment.ui;

import org.assignment.entities.Customer;
import org.assignment.enums.ProductType;
import org.assignment.enums.ResponseStatus;
import org.assignment.enums.Roles;

import org.assignment.exceptions.CustomerNotFoundException;
import org.assignment.exceptions.UnauthorizedOperationException;

import org.assignment.services.AdminService;

import org.assignment.serviceimlementation.AdminServiceImplementation;

import org.assignment.util.ColorCodes;
import org.assignment.util.LogUtil;
import org.assignment.util.Response;

import javax.swing.*;
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
                   resp = service.deleteCustomer(isSuperAdmin);
                    break;
                case "8":

                     resp = service.grantAccess(isSuperAdmin);
                        break;
                    case "9":
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
    private Response fetchProductByType() {
        String operation;
        Response response = null;
        boolean isFinished = false;
        while (!isFinished) {
            System.out.print("Press : \n 1 for PHONE \n 2 for FURNITURE \n 3 for APPLIANCES \n 4 for MAKEUP \n 5 for CLOTHING \n operation : ");
            operation = sc.nextLine();
            switch (operation) {
                case "1":
                    response = helperForGetProductsByType(ProductType.PHONE);
                    isFinished = true;
                    break;
                case "2":
                    response = helperForGetProductsByType(ProductType.FURNITURE);
                    isFinished = true;
                    break;
                case "3":
                    response = helperForGetProductsByType(ProductType.APPLIANCES);
                    isFinished = true;
                    break;
                case "4":
                    response = helperForGetProductsByType(ProductType.MAKEUP);
                    isFinished = true;
                    break;
                case "5":
                    response = helperForGetProductsByType(ProductType.CLOTHING);
                    isFinished = true;
                    break;
                default:
                    System.out.println(ColorCodes.RED + "Wrong operation choosen" + ColorCodes.RESET);//executed if the type is of the defined main.enums.
            }
        }
        return  response;
    }
    private Response deleteCustomer(boolean authorized){
        if (!authorized) {//checks if the operation performed by superadmin or not.
            return new Response(null, "Your are not authorized to access this service");
        }
        List<Customer> allCustomer = null;
        try {
//replace below line from customerserviceImp -> getAllCustomers.
            allCustomer = customerRepository.getCustomers().stream().filter(c -> c.getRole() == Roles.CUSTOMER).toList();
            if (allCustomer.isEmpty()) {
                return new Response(null, "No customers are present");
            }
        } catch (CustomerNotFoundException e) {
            return  new Response(null, e.getLocalizedMessage());
        }catch (SQLException e){
            return  LogUtil.logError(e.getLocalizedMessage());
        }

        System.out.println(ColorCodes.BLUE + "Customers : " + allCustomer + ColorCodes.RESET);
        System.out.print("Provide customer id : ");
        Long cid = sc.nextLong();
//give customer id to service layer to delete the customer.
    }


}