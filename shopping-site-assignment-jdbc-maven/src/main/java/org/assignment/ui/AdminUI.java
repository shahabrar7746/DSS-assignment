package org.assignment.ui;

import org.assignment.entities.Customer;
import org.assignment.enums.ProductType;
import org.assignment.enums.ResponseStatus;
import org.assignment.enums.Roles;

import org.assignment.exceptions.UnauthorizedOperationException;

import org.assignment.services.AdminService;

import org.assignment.services.CustomerService;
import org.assignment.util.ColorCodes;
import org.assignment.util.Response;

import java.util.*;

public class AdminUI extends UI {
    private final Scanner sc = new Scanner(System.in);
    private final AdminService service;
    private final CustomerService customerService;

    public AdminUI(AdminService service, CustomerService customerService) {
        this.service = service;
        this.customerService = customerService;
    }

    public void initAdminServices(Customer admin) {
        boolean isSuperAdmin = Objects.equals(admin.getRole(), Roles.SUPER_ADMIN);
        String message = isSuperAdmin ? "SUPER_ADMIN" : "ADMIN";
        System.out.println(ColorCodes.GREEN + "*********WELCOME-" + message + "****************" + ColorCodes.RESET);

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
                option.add("Press 7 to delete customer");
                option.add("Press 8 to grant admin access to customer");
                option.add("Press 9 to revoke access from customer");
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
                    resp = getCustomerById();
                    break;
                case "4":
                    resp = service.getAllDeliveredOrders();
                    break;
                case "5":
                    resp = getProductByType();
                    break;
                case "6":
                    resp = service.fetchAllAdmins();
                    break;
                case "7":
                    resp = deleteCustomer();
                    break;
                case "8":
                    resp = grantAccess();
                    break;
                case "9":
                    resp = revokeAccess();
                    break;
                case "back", "BACK":
                    resp = new Response("Going back");
                    isExit = true;
                    break;
                default:
                    System.err.println();
                    resp = new Response(null, "Unsupported operation");
            }
            if (resp.getStatus() == ResponseStatus.ERROR) {
                System.out.println(ColorCodes.RED + "ERROR : " + resp.getError() + ColorCodes.RESET);
            } else if (resp.getStatus() == ResponseStatus.SUCCESSFUL) {
                System.out.println(ColorCodes.RED + response + resp.getData() + ColorCodes.RESET);
            }
            if (isExit) {
                break;
            }
        }

    }

    /**
     * Used to check if the thread has authority to access below methods.
     *
     * @param isSuperAdmin used to indicate whether the user can access the requested service or not.
     * @throws UnauthorizedOperationException if the user does not have permission to access the service.
     */
    private void authorize(boolean isSuperAdmin) throws UnauthorizedOperationException {
        if (Boolean.FALSE.equals(isSuperAdmin)) {
            throw new UnauthorizedOperationException("Your are not authorized to access this service");
        }
    }

    private Response getProductByType() {

        String operation;
        Response response = null;
        boolean isFinished = false;
        while (!isFinished) {
            System.out.print("Press : \n 1 for PHONE \n 2 for FURNITURE \n 3 for APPLIANCES \n 4 for MAKEUP \n 5 for CLOTHING \n operation : ");
            operation = sc.nextLine();
            switch (operation) {
                case "1":
                    response = service.getProductsByType(ProductType.PHONE);
                    isFinished = true;
                    break;
                case "2":
                    response = service.getProductsByType(ProductType.FURNITURE);
                    isFinished = true;
                    break;
                case "3":
                    response = service.getProductsByType(ProductType.APPLIANCES);
                    isFinished = true;
                    break;
                case "4":
                    response = service.getProductsByType(ProductType.MAKEUP);
                    isFinished = true;
                    break;
                case "5":
                    response = service.getProductsByType(ProductType.CLOTHING);
                    isFinished = true;
                    break;
                default:
                    System.out.println(ColorCodes.RED + "Wrong operation choosen" + ColorCodes.RESET);//executed if the type is of the defined main.enums.
            }
        }
        return response;//returns list of product of same type.
    }

    private Response grantAccess() {
        Response customerResponse = service.getAllCustomer();
        if (customerResponse.getStatus() == ResponseStatus.ERROR) {
            return customerResponse;
        }
        System.out.println(ColorCodes.BLUE + "Admins : " + customerResponse.getData() + ColorCodes.RESET);
        System.out.print("Enter Customer id to whom you want to grant access to : ");
        String cid = sc.nextLine();
        Response isCustomer = service.customerExists(Long.valueOf(cid));
        if (isCustomer.getStatus() == ResponseStatus.ERROR) {
            return isCustomer;
        }
        boolean condition = (boolean) isCustomer.getData();
        int count = 5;
        while (!condition && count-- > 0) {
            System.out.println("Wrong customer id, enter correct customer id");
            System.out.print(ColorCodes.BLUE + "Enter Customer id to whom you want to grant access to : " + ColorCodes.RESET);
            cid = sc.nextLine();
            isCustomer = service.customerExists(Long.valueOf(cid));
            if (isCustomer.getStatus() == ResponseStatus.ERROR) {
                return isCustomer;
            }
            condition = (boolean) isCustomer.getData();
        }
        Response authResponse = authenticate();
        return authResponse.getStatus() == ResponseStatus.ERROR ? authResponse : service.grantAccess(Long.valueOf(cid));
    }

    private Response authenticate() {
        System.out.println("please provide the super admin password for further action : ");
        String password = sc.nextLine();
        int count = 5;
        Response response = null;
        while (!service.authenticateSuperAdmin(password) && count-- > 0) {
            System.out.println("Incorrect password please try again...");
            password = sc.nextLine();
        }
        if (!service.authenticateSuperAdmin(password)) {
            response = new Response(null, "Incorrect password");
        }
        if (response == null && count == 0) {
            response = new Response(null, "try limit exceed");
        }
        return response == null ? new Response("Authenticated") : response;
    }

    private Response revokeAccess() {
        Response admins = service.fetchAllAdmins();
        if (admins.getStatus() == ResponseStatus.ERROR) {
            return admins;
        }
        System.out.println(ColorCodes.BLUE + "Admins : " + admins.getData() + ColorCodes.RESET);
        System.out.print("Enter Customer id to whom you want to revoke access to : ");
        String cid = sc.nextLine();
        Optional<Customer> customer = Optional.empty();//fetches admins based on id.
        Response adminResponse = service.isAdmin(Long.valueOf(cid));
        if (adminResponse.getStatus() == ResponseStatus.ERROR) {
            return adminResponse;
        }
        boolean condition = (boolean) adminResponse.getData();
        while (!condition ) {
            System.out.println("Wrong customer id, enter correct customer id");
            System.out.print("Enter Customer id to whom you want to revoke access to : ");
            cid = sc.nextLine();//keeps on requesting id if no object is found for the previous id, keeps on going until count is not equals to 0 or less than 0
            admins = service.isAdmin(Long.valueOf(cid));
            if (admins.getStatus() == ResponseStatus.ERROR) {
                return admins;
            }
            condition = (Boolean) admins.getData();
        }

        Response authResponse = authenticate();
        return authResponse.getStatus() == ResponseStatus.ERROR ? authResponse : service.revokeAccess(Long.valueOf(cid));
    }

    private Response getCustomerById() {
        Response response = service.getAllCustomer();
        if (response.getStatus() == ResponseStatus.ERROR) {
            return response;
        }
        List<Customer> data = (List<Customer>) response.getData();
        if (data.isEmpty()) {
            return new Response(null, "No customers are there");
        }
        Optional<Customer> customer = Optional.empty();
        System.out.print("Please provide the id of Customer : ");
        Long id = sc.nextLong();
        return service.getCustomerById(id);
    }

    private Response deleteCustomer() {
        List<Customer> allCustomer = null;
        Response customerResponse = service.getAllCustomer();
        if (customerResponse.getStatus() == ResponseStatus.ERROR) {
            return customerResponse;
        }
        System.out.println(ColorCodes.BLUE + "Customers : " + allCustomer + ColorCodes.RESET);
        System.out.print("Provide customer id : ");
        Long cid = sc.nextLong();
        return service.deleteCustomer(cid);
    }
}