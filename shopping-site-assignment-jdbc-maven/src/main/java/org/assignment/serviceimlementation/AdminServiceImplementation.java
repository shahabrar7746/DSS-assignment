package org.assignment.serviceimlementation;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assignment.entities.Customer;

import org.assignment.entities.Product;

import org.assignment.enums.ProductType;
import org.assignment.enums.ResponseStatus;
import org.assignment.enums.Roles;

import org.assignment.exceptions.*;

import org.assignment.repository.interfaces.OrderRepository;
import org.assignment.repository.interfaces.ProductRepository;

import org.assignment.repositoryhibernateimpl.CustomerRepoHibernateImpl;
import org.assignment.repositoryhibernateimpl.OrderRepoHibernateImpl;
import org.assignment.repositoryhibernateimpl.ProductRepoHibernateImpl;

import org.assignment.services.AdminService;

import org.assignment.repository.interfaces.CustomerRepository;

import org.assignment.util.ColorCodes;
import org.assignment.util.LogUtil;
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
    private final Logger logger = LogManager.getLogger(this.getClass());
    private AdminServiceImplementation() {
        init();
    }
    private void init(){
        this.productRepository =  ProductRepoHibernateImpl.getInstance();
        this.customerRepository = CustomerRepoHibernateImpl.getInstance();
        this.sc = new Scanner(System.in);
        this.orderRepository = OrderRepoHibernateImpl.getInstance();
    }
    private static AdminServiceImplementation service;

    public static AdminServiceImplementation getInstance() {
        if (service == null) {
            service = new AdminServiceImplementation();
        }
        return service;
    }

    @Override
    public Response getCustomerById(Long id) {
Optional<Customer> customer = null;
        try {
            customer = customerRepository.fetchById(id);
        }catch (SQLException e) {
            return  LogUtil.logError(e.getLocalizedMessage());
        } catch (CustomerNotFoundException  e) {
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
            allCustomer = customerRepository
                    .getCustomers()
                    .stream()
                    .filter(c -> c.getRole() == Roles.CUSTOMER)
                    .toList();
        } catch (CustomerNotFoundException e) {
            return new Response(null, e.getLocalizedMessage());
        } catch (SQLException e) {
            return  LogUtil.logError(e.getLocalizedMessage());

        }
        if (allCustomer.isEmpty()) {
            return new Response(null, "Customer main repository is empty"); //executed if no customer object is found
        }
        return new Response(allCustomer);// returns all the customer.
    }


    @Override
    public Response getAllProdcuts() {
        List<Product> allProducts = null;
        try {
            allProducts = productRepository.fetchProducts();
        } catch (NoProductFoundException e) {
            return new Response(null, e.getLocalizedMessage());
        } catch (SQLException e) {
            return  LogUtil.logError(e.getLocalizedMessage());
        }
        if (allProducts.isEmpty()) {
            //executed if no product object is found
            return new Response(null, "No Products found");
        }
        return new Response(allProducts);// returns all the product.
    }

    /**
     * @param type to search for product of same type.
     * @return List of Product of same type as provided by caller.
     * @throws NoProductFoundException if no product is found of same type.
     * @see #getAllProdcuts()
     */

    public Response getProductsByType(ProductType type) {
        List<Product> products = null;// fetches List of product to be searched in.
        try {
            products = productRepository.fetchProducts();
        } catch (NoProductFoundException e) {
            return new Response(null, e.getLocalizedMessage());
        } catch (SQLException e) {
            return LogUtil.logError(e.getStackTrace());
        }
        Map<ProductType, List<Product>> map = products
                .stream()
                .collect(Collectors.groupingByConcurrent(Product::getType));
        if (!map.containsKey(type)) {
            return new Response(null, "No product found for this category"); // throws exception if no product is found for the provided type.
        }
        return new Response(map.get(type));
    }

    @Override
    public Response getAllDeliveredOrders() {
        Response response = null;
        try {
            response = new Response(orderRepository.getAllDeliveredOrders());
        } catch (SQLException e) {
            response =  LogUtil.logError(e.getLocalizedMessage());
        }catch (CustomerNotFoundException | OrderNotFoundException | NoProductFoundException e){
            response = new Response(null, e.getLocalizedMessage());
        }
        return response;
    }


    @Override
    public Response grantAccess(Long id) {
        Optional<Customer> customer = null;
        try {
             customer = customerRepository.fetchById(id);
            if ( customer.get().getRole() == Roles.ADMIN) {
                return  new Response(null, "Already a Admin");//indicates if the customer is already a admin.
            }
            if (customer.get().getRole() == Roles.SUPER_ADMIN) {//executed if super admins authority is tried to overridden.
                return new Response(null, "action not allowed");
            }
            customer.get().setRole(Roles.ADMIN);
            customerRepository.updateCustomer(customer.get());
        }catch (CustomerNotFoundException e){
            return new Response(e.getLocalizedMessage());
        }catch (Exception e){
            return LogUtil.logError(e.getStackTrace());
        }
        return new Response("Access granted !!");

    }


    @Override
    public Response revokeAccess(Long id)  {
        Response response = null;
        try {
            Optional<Customer> customer = customerRepository.fetchAdminById(id);
            if (customer.isPresent() && customer.get().getRole() == Roles.SUPER_ADMIN) {
                return new Response(null, "Cannot perform action");
            }
            if (customer.isPresent() && customer.get().getRole() == Roles.CUSTOMER) {
                return new Response(null, "The chosen user is already a customer");
            }
            customer.get().setRole(Roles.CUSTOMER);
            customerRepository.updateCustomer(customer.get());//critical section.
            response = new Response(null, "Access revoked");
        }catch (Exception e){
         response = LogUtil.logError(e.getStackTrace());
        }
        return  response;
    }
    /**
     * Below method requests for super admin password for variable 'count' times which is default set to 6.
     * If number of tries exceeds the default set count then 'TrialLimitExceedException' exception is thrown.
     *
     * @return true if successfully authenticated.
     * @throws WrongPasswordException    if the password is incorrect even on last try.
     * @throws TrialLimitExceedException if the try limit exceeds.
     */
   public   boolean authenticateSuperAdmin(String password){
      return password.equals(SUPER_ADMIN_PASSWORD);
    }

    @Override
    public Response customerExists(Long id) {
        Response response = null;
        try {
            response = new Response(customerRepository.fetchById(id).get().getRole() == Roles.CUSTOMER);
        }catch (Exception e){
            response = LogUtil.logError(e.getStackTrace());
        }
        return response;
    }

    @Override
    public Response isAdmin(Long id) {
       Response response = null;
       try {
           response = new Response(customerRepository.fetchAdminById(id).isPresent());
       }catch (Exception e){
           response = LogUtil.logError(e.getStackTrace());
       }
       return response;
    }

    @Override
    public Response deleteCustomer(Long cid) {
        try {
            Optional<Customer> customer = customerRepository.fetchById(cid);//fetch customer by provided id.
            if (customer.isEmpty() || customer.get().getRole() != Roles.CUSTOMER) {
                return new Response(null, "No Customer object found");
            }
            customerRepository.removeCustomer(customer.get());//critical section.
        }catch (SQLException e){
            return LogUtil.logError(e.getStackTrace());
        }
        catch (CustomerNotFoundException e) {
            return  LogUtil.logError(e.getLocalizedMessage());
        }
        return new Response("Customer deleted");
    }

    @Override
    public Response fetchAllAdmins() {//fetches all admins excluding super admin and customer.
        List<Customer> admins = new ArrayList<>();
        try {
            admins = customerRepository.getCustomers()
                    .stream()
                    .filter(c -> c.getRole() == Roles.ADMIN)
                    .toList();
            if (admins.isEmpty()) {
                return new Response(null, "No admin found");//throws NoAdminFoundException if no admin found CustomerRepository.
            }
        } catch (SQLException e) {
            return  LogUtil.logError(e.getLocalizedMessage());
        }
        catch (CustomerNotFoundException e){
            return new Response(null, e.getLocalizedMessage());
        }
        return new Response(admins);//returns list of admins if any found.
    }

}