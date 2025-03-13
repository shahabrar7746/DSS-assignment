package org.assignment.serviceimlementation;

import org.assignment.entities.Customer;
import org.assignment.entities.Order;
import org.assignment.entities.Product;

import org.assignment.enums.ProductType;
import org.assignment.enums.ResponseStatus;
import org.assignment.enums.Roles;

import org.assignment.exceptions.*;

import org.assignment.repository.interfaces.OrderRepository;
import org.assignment.repository.interfaces.ProductRepository;


import org.assignment.repositoryjdbcimpl.CustomerRepositoryImpl;
import org.assignment.repositoryjdbcimpl.OrderRepositoryImpl;
import org.assignment.repositoryjdbcimpl.ProductRepositoryImpl;
import org.assignment.services.AdminService;

import org.assignment.repository.interfaces.CustomerRepository;

import org.assignment.util.ColorCodes;
import org.assignment.util.Response;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class AdminServiceImplementation implements AdminService {
    private static final String SUPER_ADMIN_PASSWORD = System.getenv("SUPER_ADMIN_PASSWORD");
    private  Scanner sc;
    private  ProductRepository productRepository;
    private  CustomerRepository customerRepository;
    private  OrderRepository orderRepository;

    private AdminServiceImplementation() { // TODO update constructor
      init();
    }
private void init(){
    this.productRepository = new ProductRepositoryImpl();
    this.customerRepository = new CustomerRepositoryImpl();
    this.sc = new Scanner(System.in);
    this.orderRepository = new OrderRepositoryImpl();
}
    private static AdminServiceImplementation service;

    public static AdminServiceImplementation getInstance() {
        if (service == null) {
            service = new AdminServiceImplementation();
        }
        return service;
    }


    @Override
    public Response getProductsByType() {


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
        return response;//returns list of product of same type.
    }

    @Override
    public Response getCustomerById() {
        try {
            Response response = getAllCustomer();
            if(response.getStatus() == ResponseStatus.ERROR){
                return response;
            }
            List<Customer> data = (List<Customer>) getAllCustomer().getData();
            if (data.isEmpty()) {
                return new Response(null, "No customers are there");
            }
        } catch (Exception e) {
          return new Response(null, e.getLocalizedMessage());
        }
        Optional<Customer> customer = Optional.empty();
                System.out.print("Please provide the id of Customer : ");
        Long id = sc.nextLong();
        try {
           customer = customerRepository.fetchById(id);
        } catch (CustomerNotFoundException | SQLException e) {
           return new Response(null, e.getLocalizedMessage());
        }
        if ( customer.isEmpty() || customer.get().getRole() != Roles.CUSTOMER) {
          return new Response(null, "No customer found");
        }
        return new Response(customer.get());// returns the found customer.
    }

    @Override
    public Response getAllCustomer() {
        List<Customer> allCustomer = null;
        try {
            allCustomer = customerRepository.getCustomers().stream().filter(c -> c.getRole() == Roles.CUSTOMER).toList();
        } catch (Exception e) {
            return new Response(null, e.getLocalizedMessage());
        }
        if (allCustomer.isEmpty()) {
            return new Response(null, "Customer main.repository is empty"); //executed if no customer object is found
        }
        return new Response(allCustomer);// returns all the customer.
    }


    @Override
    public Response getAllProdcuts() {
        List<Product> allProducts = null;
        try {
            allProducts = productRepository.fetchProducts();
        } catch (Exception e) {
            return new Response(null, e.getLocalizedMessage());
        }
        if (allProducts.isEmpty()) {
            //executed if no product object is found
            return new Response(null, "NO Products found");
        }
        return new Response(allProducts);// returns all the product.
    }

    /**
     * @param type to search for product of same type.
     * @return List of Product of same type as provided by caller.
     * @throws NoProductFoundException if no product is found of same type.
     * @see #getAllProdcuts()
     */

    private Response helperForGetProductsByType(ProductType type) {
        List<Product> products = null;// fetches List of product to be searched in.
        try {
            products = productRepository.fetchProducts();
        } catch (Exception e) {
            return new Response(e.getLocalizedMessage());
        }
        Map<ProductType, List<Product>> map = products.stream().collect(Collectors.groupingByConcurrent(Product::getType));
        if (!map.containsKey(type)) {
            return new Response(null, "No product found for this category"); // throws exception if no product is found for the provided type.
        }
        return new Response(map.get(type));
    }

    @Override
    public Response getAllDeliveredOrders() {

        try {
            return new Response(orderRepository.getAllDeliveredOrders());
        } catch (Exception e) {
            return new Response(null, e.getLocalizedMessage());
        }
    }


    @Override
    public Response grantAccess(boolean isAuthorized) {
        if (!isAuthorized) {//checks if the operation performed by superadmin or not.
            return new Response(null, "Your are not authorized to access this service");//if not, throws this exception.
        }
        try {
            System.out.println(ColorCodes.BLUE + "Admins : " + getAllCustomer().getData() + ColorCodes.RESET);
        } catch (Exception e) {
            return new Response(null, e.getLocalizedMessage());
        }
        System.out.print("Enter Customer id to whom you want to grant access to : ");
        String cid = sc.nextLine();
        int count = 3;
        Optional<Customer> customer = Optional.empty();
        try {

            customer = customerRepository.fetchById(Long.valueOf(cid));
            while (customer.isEmpty() && count-- >= 0) {
                System.out.println("Wrong customer id, enter correct customer id");
                System.out.print(ColorCodes.BLUE + "Enter Customer id to whom you want to grant access to : " + ColorCodes.RESET);
                cid = sc.nextLine();
                customer = customerRepository.fetchById(Long.valueOf(cid));//fetches customer by id.
            }
        } catch (Exception e) {
            return new Response(null, e.getLocalizedMessage());
        }
        if (count < 0) {
            return new Response(null, "You have exceeded the try limit");// thrown if the try 'count' <= 0.
        }
        if(customer.isEmpty()){
            return  new Response(null, "No customer found");
        }
        if ( customer.get().getRole() == Roles.ADMIN) {
            return  new Response(null, "Already a Admin");//indicates if the customer is already a admin.

        }
        if (customer.get().getRole() == Roles.SUPER_ADMIN) {//executed if super admins authority is tried to overridden.
           return new Response(null, "action not allowed");

        }
        try {
            if (authenticateSuperAdmin()) {//if 'true' lets you perform critical operation.
                customer.get().setRole(Roles.ADMIN);//critical operation.
                customerRepository.updateCustomer(customer.get());
            }
        }catch (Exception e){
            return  new Response(null, e.getLocalizedMessage());
        }
        return new Response("Access granted !!");

    }


    @Override
    public Response revokeAccess(boolean isAuthorized)  {
        if (!isAuthorized) {//checks if the operation performed by superadmin or not.
           return new Response(null, "Your are not authorized to access this service");
        }
        System.out.println(ColorCodes.BLUE + "Customers : " + fetchAllAdmins() + ColorCodes.RESET);
        System.out.print("Enter Customer id to whom you want to revoke access to : ");
        String cid = sc.nextLine();
        Optional<Customer> customer = Optional.empty();//fetches admins based on id.

        int count = 3;//sets try limit.
        while (customer.isEmpty() && count-- >= 0) {
            System.out.println("Wrong customer id, enter correct customer id");
            System.out.print("Enter Customer id to whom you want to revoke access to : ");
            cid = sc.nextLine();//keeps on requesting id if no object is found for the previous id, keeps on going until count is not equals to 0 or less than 0
            try {
                customer = customerRepository.fetchAdminById(Long.valueOf(cid));
            } catch (SQLException e) {
                return new Response(null, e.getLocalizedMessage());
            }
        }
        if (customer.isEmpty()) {
            return new Response(null, "No admin found");

        }
        if (count < 0) {
          return new Response(null, "Trial limit exceed");
        }
        if (customer.isPresent() && customer.get().getRole() == Roles.SUPER_ADMIN) {
           return new Response(null, "Cannot perform action");
        }
        if (customer.isPresent() && customer.get().getRole() == Roles.CUSTOMER) {
           return new Response(null, "The chosen user is already a customer");
        }

        try {
            if (customer.isPresent() && authenticateSuperAdmin()) {//checks performing critical section.
                customer.get().setRole(Roles.CUSTOMER);
                customerRepository.updateCustomer(customer.get());//critical section.
            }
        } catch (Exception e) {
            return new Response(null, e.getLocalizedMessage());
        }
        return  new Response("Access revoked");
    }


    @Override
    public Response cancelOrder(boolean isAuthorized) {
        if (!isAuthorized) {//checks if the operation performed by superadmin or not.
           return new Response(null, "Your are not authorized to access this service");
        }
        List<Order> orders = null;//gets all orders present in the main.repository.
        try {
            orders = orderRepository.getAllOrders();
        } catch (Exception e) {
            return new Response(null, e.getLocalizedMessage());
        }
        if (orders.isEmpty()) {
            return new Response(null, orders.size() + " orders found to be cancelled");// throws exception if no orders are present in the OrderRepository.
        }
        System.out.println(ColorCodes.BLUE + "Orders : " + orders + ColorCodes.RESET);
        System.out.print("Enter order id to be cancelled : ");
        Optional<Order> order = Optional.empty();
        try {
            orders = orderRepository.getAllOrders();
        } catch (Exception e) {
            return new Response(null, e.getLocalizedMessage());
        }
      if(order.isEmpty()){
          return new Response(null, "The order id is incorrect");
      }
      try {
          if (authenticateSuperAdmin()) {//executed if Order object is found for the id.
              orderRepository.cancelOrder(order.get());
          }
      } catch (Exception e) {
          return new Response(null,e.getLocalizedMessage());
      }
      return new Response("Order cancelled successfully.");
     // thrown if no Order is found.
    }

    /**
     * Below method requests for super admin password for variable 'count' times which is default set to 6.
     * If number of tries exceeds the default set count then 'TrialLimitExceedException' exception is thrown.
     *
     * @return true if successfully authenticated.
     * @throws WrongPasswordException    if the password is incorrect even on last try.
     * @throws TrialLimitExceedException if the try limit exceeds.
     */
    private boolean authenticateSuperAdmin() throws TrialLimitExceedException, WrongPasswordException {
        int count = 6;
        String password = "";

        while (!password.equals(SUPER_ADMIN_PASSWORD) && count-- > 0) {
            if (count < 5) {
                System.err.println("Try again");
            }
            System.out.println("Provide Super Admin password to procced further\nPassword : ");
            password = sc.nextLine();
        }
        if (count <= 0) {
            throw new TrialLimitExceedException("you have tried too many times, please try again later");
        }
        if (!password.equals(SUPER_ADMIN_PASSWORD)) {
            throw new WrongPasswordException("you have entered the wrong password");
        }
        return true;
    }

    @Override
    public Response deleteCustomer(boolean isAuthorized) {
        if (!isAuthorized) {//checks if the operation performed by superadmin or not.
            return new Response(null, "Your are not authorized to access this service");
        }
        List<Customer> allCustomer = null;
                try {

                    allCustomer = customerRepository.getCustomers().stream().filter(c -> c.getRole() == Roles.CUSTOMER).toList();
                    if (allCustomer.isEmpty()) {
                        return new Response(null, "No customers are present");
                    }
                } catch (Exception e) {
                    return  new Response(null, e.getLocalizedMessage());
                }

        System.out.println(ColorCodes.BLUE + "Customers : " + allCustomer + ColorCodes.RESET);
        System.out.print("Provide customer id : ");
        Long cid = sc.nextLong();
        try {
            Optional<Customer> customer = customerRepository.fetchById(cid);//fetch customer by provided id.
            if (customer.isEmpty() || customer.get().getRole() != Roles.CUSTOMER) {
                return new Response(null, "No Customer object found");
            }

            if (authenticateSuperAdmin()) { //autheticates befores performing critical section. on any negative scenario a exception is thrown. or else true.
                customerRepository.removeCustomer(customer.get());//critical section.

            }
        } catch (Exception e) {
            return new Response(null, e.getLocalizedMessage());
        }
        return new Response("Customer deleted");

    }

    @Override
    public Response fetchAllAdmins() {//fetches all admins excluding super admin and customer.
     List<Customer> admins = new ArrayList<>();
        try {
        admins = customerRepository.getCustomers().stream().filter(c -> c.getRole() == Roles.ADMIN).toList();
         if (admins.isEmpty()) {
             return new Response(null, "No admin found");//throws NoAdminFoundException if no admin found CustomerRepository.
         }
     } catch (Exception e) {
         return new Response(null, e.getLocalizedMessage());
     }
        return new Response(admins);//returns list of admins if any found.
    }


}
