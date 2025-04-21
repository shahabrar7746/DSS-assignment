package org.assignment.ui;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import org.assignment.entities.Order;
import org.assignment.entities.User;
import org.assignment.enums.ProductType;
import org.assignment.enums.ResponseStatus;
import org.assignment.enums.Roles;

import org.assignment.services.AdminService;

import org.assignment.services.UserService;
import org.assignment.services.OrderService;
import org.assignment.services.ProductService;
import org.assignment.util.ColorCodes;
import org.assignment.util.Constants;
import org.assignment.util.Inputs;
import org.assignment.util.Response;
import org.assignment.wrappers.CustomerWrapper;
import org.assignment.wrappers.ProductWrapper;
import org.assignment.wrappers.SellerWrapper;

import java.util.*;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class AdminUI extends UI {
    private final Scanner sc;
    private final AdminService service;

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;

    @Override
    public void initAdminServices(User admin) {
        boolean isSuperAdmin = Objects.equals(admin.getRole(), Roles.SUPER_ADMIN);
        String message = isSuperAdmin ? "SUPER_ADMIN" : "ADMIN";
        System.out.println(ColorCodes.GREEN + "*********WELCOME-" + message + "****************" + ColorCodes.RESET);

        String operation = "";
        while (Boolean.FALSE.equals(operation.equalsIgnoreCase("0"))) {
            boolean isExit = false;

            List<String> option = new ArrayList<>(); // List.of
            option.add("Press 1 to display all the user");
            option.add("Press 2 to display all products");
            option.add("Press 3 to find user on basis of their id");
            option.add("Press 4 to display all orders");
            option.add("Press 5 to display the products by their category");
            option.add("Press 6 to display all admins");
            option.add("Press 7 to add product for seller");
            if (isSuperAdmin) {//todo
                option.add("Press 8 to grant admin access to user");
                option.add("Press 9 to revoke access from user");
            }
            option.add("Enter '0' to go to previous page");
            displayOptions(option);
            operation = Inputs.getStringInputs(sc);

            Response resp;
            switch (operation) {
                case "1":
                    resp = userService.getAllCustomer();
                    if (resp.getStatus() == ResponseStatus.SUCCESSFUL) {
                        printCustomers((List<User>) resp.getData());
                        resp = null;
                    }
                    break;
                case "2":
                    resp = productService.getAllProduct();
                    break;
                case "3":
                    resp = getCustomerByEmail("Enter customer email to view his/her details : ");
                    if (resp.getStatus() == ResponseStatus.SUCCESSFUL) {
                       displaySingleCustomer((Optional<User>) resp.getData());
                        resp = null;
                    }
                    break;
                case "4":
                    resp = getOrdersByCustomer();
                    if(resp.getStatus() == ResponseStatus.SUCCESSFUL)
                    {
                        printOrdersForRole(true, (List<Order>) resp.getData());
                        resp = null;
                    }
                    break;
                case "5":
                    resp = getProductByType();
                    if (resp.getStatus() == ResponseStatus.SUCCESSFUL) {
                        printProducts((List<ProductWrapper>) resp.getData());
                        resp = null;
                    }
                    break;
                case "6":
                    resp = userService.fetchAllAdmins();
                    break;
                case "7":
                    resp = addProduct();
                    break;
                case "8":
                    resp = grantAccess();
                    break;
                case "9":
                    resp = revokeAccess();
                    break;
                case "0":
                    resp = new Response(ResponseStatus.SUCCESSFUL, "Going back", null);
                    isExit = true;
                    break;
                default:
                    resp = new Response(ResponseStatus.ERROR, null, "Unsupported operation");
            }

            printResponse(resp);

            if (isExit) {
                break;
            }
        }

    }
    private void displaySingleCustomer(Optional<User> customerOptional)
    {
        if(customerOptional.isEmpty())
        {
            System.out.println(ColorCodes.RED + "Invalid customer email " + ColorCodes.RESET);
            return;
        }
        printCustomers(List.of(customerOptional.get()));
    }

    private Response addProduct() {
        Response response = null;
        while (response == null || response.getStatus() == ResponseStatus.ERROR) {
            try {
                System.out.println(ColorCodes.GREEN + "Please provide product name" + ColorCodes.RESET);
                String productName = Inputs.getStringInputs(sc).toUpperCase();
                boolean condition = Boolean.logicalOr(StringUtils.isAllBlank(productName),StringUtils.isNumeric(productName));

                while (condition)
                {
                    System.out.println(ColorCodes.RED + "product name cannot be blank" + ColorCodes.RESET);
                    System.out.println(ColorCodes.GREEN + "Please provide product name" + ColorCodes.RESET);
                    productName = Inputs.getStringInputs(sc).toUpperCase();
                    condition = Boolean.logicalOr(StringUtils.isAllBlank(productName),StringUtils.isNumeric(productName));
                }
                System.out.println(ColorCodes.GREEN + "Please choose Currency" + ColorCodes.RESET);
                printAndDisplayOptionsForCurrencies();
                int currencyIndex = Inputs.getIntegerInput(sc);

                System.out.println(ColorCodes.GREEN + "Please choose Product Type" + ColorCodes.RESET);
                printAndDisplayOptionsForProductTypes();
                int productTypeIndex = Inputs.getIntegerInput(sc);

                double price = 0.0;
                while (price <= 0.0) {
                    System.out.println(ColorCodes.RED + "Please provide price of the product(must be greater than 0) " + ColorCodes.RESET);
                    price = Inputs.getDoubleInput(sc);
                }
                Response sellerResponse = userService.fetchAllSellers();
                if (sellerResponse.getStatus() == ResponseStatus.SUCCESSFUL && sellerResponse.getData() instanceof List) {
                    printSellerWrappers((List<SellerWrapper>) sellerResponse.getData());
                }
                if (sellerResponse.getStatus() == ResponseStatus.ERROR) {
                    return sellerResponse;
                }

                System.out.println(ColorCodes.GREEN + "Provide seller id" + ColorCodes.RESET);
                long seller = Inputs.getLongInput(sc);
                int stock = 0;
                while (stock <= 0) {
                    System.out.println(ColorCodes.RED + "Please provide stocks of the product(must be greater than 0) " + ColorCodes.RESET);
                    stock = Inputs.getIntegerInput(sc);
                }
                response = productService.addProduct(productName, seller, productTypeIndex, currencyIndex, price, stock);
                if (response.getStatus() == ResponseStatus.ERROR) {
                    printResponse(response);
                }

            } catch (InputMismatchException e) {
                System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
                sc.next();
            }
        }

        return response;
    }

    private Response getOrdersByCustomer() {
        Response allCustomerResponse = userService.getAllMaskedCustomer();
        if (allCustomerResponse.getStatus() == ResponseStatus.ERROR) {
            return allCustomerResponse;
        }
        printCustomerWrappers((List<CustomerWrapper>) allCustomerResponse.getData());
        String mesaage = "Please provide email of user whose order you want to display\nEmail:";
        Response findByEmailResponse = getCustomerByEmail(mesaage);
        while (Boolean.logicalOr(ObjectUtils.anyNull(findByEmailResponse),
                Boolean.TRUE.equals(findByEmailResponse.getStatus() == ResponseStatus.SUCCESSFUL))
                && (findByEmailResponse.getData() instanceof Optional<?> customerOptional
                && customerOptional.isEmpty())) {
             findByEmailResponse = getCustomerByEmail(mesaage);
        }
        if (Boolean.TRUE.equals(findByEmailResponse.getStatus() == ResponseStatus.ERROR)) {
            return findByEmailResponse;
        }
        Optional<User> customerTobeFound = (Optional<User>) findByEmailResponse.getData();
        User user = customerTobeFound.get();
        return orderService.getAllOrdersByCustomer(user);
    }

    private Response getProductByType() {
        String operation;
        Response response = null;
        boolean isFinished = false;

        while (Boolean.FALSE.equals(isFinished)) {
            ProductType types[] = ProductType.values();
            for (int i = 0; i < types.length; i++) {
                System.out.println("Press " + i + " for " + types[i]);
            }
            System.out.println("Press " + types.length + " to display all product");
            operation = Inputs.getStringInputs(sc);
            if (operation.matches("-?\\d+")) {
                int index = Integer.parseInt(operation);
                if (index < 0 || index > types.length) {
                    isFinished = false;
                } else if (index == types.length) {
                    response = productService.getAllProduct();
                    isFinished = true;
                } else {
                    response = productService.getProductsByType(types[index]);
                    isFinished = true;
                }

            } else if (!isFinished) {
                System.out.println(ColorCodes.RED + "Wrong operation choosen" + ColorCodes.RESET);//executed if the type is of the defined main.enums.
            }
        }

        return response;//returns list of product of same type.
    }

    private Response grantAccess() {
        Response customerResponse = userService.getAllCustomer();

        if (customerResponse.getStatus() == ResponseStatus.ERROR) {
            return customerResponse;
        }

        System.out.println(ColorCodes.BLUE + "Admins : " + customerResponse.getData() + ColorCodes.RESET);
        System.out.print("Enter User id to whom you want to grant access to : ");
        String cid = Inputs.getStringInputs(sc);;

        Response isCustomer = userService.customerExists(Long.valueOf(cid));

        if (isCustomer.getStatus() == ResponseStatus.ERROR) {
            return isCustomer;
        }
        boolean condition = (boolean) isCustomer.getData();
        int count = 5;

        while (!condition && (count-- > 0)) {
            System.out.println("Wrong user id, enter correct user id");
            System.out.print(ColorCodes.BLUE + "Enter User id to whom you want to grant access to : " + ColorCodes.RESET);
            cid = Inputs.getStringInputs(sc);
            isCustomer = userService.customerExists(Long.valueOf(cid));
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
        String password = Inputs.getStringInputs(sc);
        int count = 5;
        Response response = null;
        while (!userService.authenticateSuperAdmin(password) && count-- > 0) {
            System.out.println("Incorrect password please try again...");
            password = Inputs.getStringInputs(sc);
        }
        if (!userService.authenticateSuperAdmin(password)) {
            response = new Response(ResponseStatus.ERROR, null, "Incorrect password");
        }
        if (response == null && count == 0) {
            response = new Response(ResponseStatus.ERROR, null, "try limit exceed");
        }
        return response == null ? new Response(ResponseStatus.SUCCESSFUL, "Authenticated", null) : response;
    }

    private Response revokeAccess() {
        Response admins = userService.fetchAllAdmins();
        if (admins.getStatus() == ResponseStatus.ERROR) {
            return admins;
        }
        System.out.println(ColorCodes.BLUE + "Admins : " + admins.getData() + ColorCodes.RESET);
        System.out.print("Enter User id to whom you want to revoke access to : ");
        String cid = Inputs.getStringInputs(sc);
        Response adminResponse = userService.isAdmin(Long.valueOf(cid));
        if (adminResponse.getStatus() == ResponseStatus.ERROR) {
            return adminResponse;
        }
        boolean condition = (boolean) adminResponse.getData();
        while (!condition) {
            try {
                System.out.println("Wrong user id, enter correct user id");
                System.out.print("Enter User id to whom you want to revoke access to : ");
                cid = Inputs.getStringInputs(sc);//keeps on requesting id if no object is found for the previous id, keeps on going until count is not equals to 0 or less than 0
                admins = userService.isAdmin(Long.valueOf(cid));
                if (admins.getStatus() == ResponseStatus.ERROR) {
                    return admins;
                }
                condition = (Boolean) admins.getData();
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
                sc.next();
            } catch (Exception e) {
                log.error("Some error occured while revoking access admin from cid ", e);
                return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
            }
        }
        Response authResponse = authenticate();
        return authResponse.getStatus() == ResponseStatus.ERROR ? authResponse
                : service.revokeAccess(Long.valueOf(cid));
    }

    @Override
    public void initAuthServices() {}

    @Override
    public void initCustomerServices(User user) {}

    private Response getCustomerByEmail(String message) {
        System.out.println(message);
        String email = Inputs.getStringInputs(sc).toUpperCase();
        if(StringUtils.isBlank(email))return new Response(ResponseStatus.ERROR, null, "Email cannot be blank");
        Response response = userService.findByEmail(email);
        return response;
    }



}