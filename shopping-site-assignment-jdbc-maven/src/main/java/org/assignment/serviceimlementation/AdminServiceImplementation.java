package org.assignment.serviceimlementation;

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

import org.assignment.util.Constants;
import org.assignment.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class AdminServiceImplementation implements AdminService {
    private static final String SUPER_ADMIN_PASSWORD = System.getenv("SUPER_ADMIN_PASSWORD");
    private static final Logger log = LoggerFactory.getLogger(AdminServiceImplementation.class);
    private ProductRepository productRepository;
    private CustomerRepository customerRepository;
    private OrderRepository orderRepository;

    public AdminServiceImplementation(ProductRepository productRepository, CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Response getCustomerById(Long id) {
        Optional<Customer> customer;
        try {
            customer = customerRepository.fetchById(id);
        } catch (SQLException e) {
            log.error("Some error occured while retrieving customer based on id : ", e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        if (customer.isEmpty() || customer.get().getRole() != Roles.CUSTOMER) {
            return new Response(null, "No customer found");
        }
        return new Response(customer.get());// returns the found customer.
    }

    @Override
    public Response getAllCustomer() {
        List<Customer> allCustomer;
        try {
            allCustomer = customerRepository
                    .getCustomers()
                    .stream()
                    .filter(c -> c.getRole() == Roles.CUSTOMER)
                    .toList();
        } catch (Exception ex) {
            log.error("error while fetching customer records: ", ex);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        if (allCustomer.isEmpty()) {
            return new Response(null, "Customer main repository is empty"); //executed if no customer object is found
        }
        return new Response(allCustomer);// returns all the customer.
    }


    @Override
    public Response getAllProdcuts() {
        List<Product> allProducts;
        try {
            allProducts = productRepository.fetchProducts();
            if(allProducts.isEmpty()) {
                new Response(null, "No Product Found...");
            }
        } catch (Exception e) {
            log.error("Error while fetching products : ", e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return new Response(allProducts);// returns all the product.
    }

    public Response getProductsByType(ProductType type) {
        List<Product> products;// fetches List of product to be searched in.
        try {
            products = productRepository.fetchProducts();
        } catch (SQLException e) {
           log.error("Error occured while retrieving product basen on types : ", e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        if(products.isEmpty()){
            return new Response(ResponseStatus.ERROR, null, "Could not find any product based on provided type");
        }
        Map<ProductType, List<Product>> map = products
                .stream()
                .collect(Collectors.groupingBy(Product::getType));

        if (!map.containsKey(type)) {
            return new Response(null, "No product found for this category"); // throws exception if no product is found for the provided type.
        }
        return new Response(map.get(type));
    }

    @Override
    public Response getAllDeliveredOrders() {
        Response response;
        try {
            response = new Response(orderRepository.getAllDeliveredOrders());
        } catch (SQLException e) {
            log.error("Some error occured while getting all delivered orders : ", e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        } catch (CustomerNotFoundException | OrderNotFoundException | NoProductFoundException e) {
            response = new Response(null, e.getLocalizedMessage());
        }
        return response;
    }


    @Override
    public Response grantAccess(Long id) {
        Optional<Customer> customer;
        try {
            customer = customerRepository.fetchById(id);
            if(customer.isEmpty()){
                return new Response(ResponseStatus.ERROR, null, "Could not find customer with given id");
            }
            if ( customer.get().getRole() == Roles.ADMIN) {
                return new Response(null, "Already a Admin");//indicates if the customer is already a admin.
            }
            if ( customer.get().getRole() == Roles.SUPER_ADMIN) {//executed if super admins authority is tried to overridden.
                return new Response(null, "action not allowed");
            }
            customer.ifPresent(c -> c.setRole(Roles.ADMIN));
            customerRepository.updateCustomer(customer.get());
        } catch (Exception e) {
            log.error("Some error occured while granting admin access to {}, stacktrace : ", id, e);
            return new Response(ResponseStatus.ERROR, null,  Constants.ERROR_MESSAGE);
        }
        return new Response("Access granted !!");
    }

    @Override
    public Response revokeAccess(Long id) {
        Response response;
        try {
            Optional<Customer> customer = customerRepository.fetchAdminById(id);
            if(customer.isEmpty()){
                return new Response(ResponseStatus.ERROR, null, "Could not find customer with given id");
            }
            if (customer.get().getRole() == Roles.SUPER_ADMIN) {
                return new Response(null, "Cannot perform action");
            }
            if (customer.get().getRole() == Roles.CUSTOMER) {
                return new Response(null, "The chosen user is already a customer");
            }
            customer.ifPresent(c -> c.setRole(Roles.CUSTOMER));
            customerRepository.updateCustomer(customer.get());//critical section.
            response = new Response(null, "Access revoked");
        } catch (Exception e) {
            log.error("Some error occured while revoke admin access from {}, stacktrace : ", id, e);
            return new Response(ResponseStatus.ERROR, null,  Constants.ERROR_MESSAGE);
        }
        return response;
    }

    public boolean authenticateSuperAdmin(String password) {
        return password.equals(SUPER_ADMIN_PASSWORD);
    }

    @Override
    public Response customerExists(Long id) {
        Response response;
        try {
            Optional<Customer> optionalCustomer = customerRepository.fetchById(id);
            if(optionalCustomer.isEmpty()){
                return new Response(ResponseStatus.ERROR, null, "Could not find customer with given id");
            }
            response = new Response(optionalCustomer.get().getRole() == Roles.CUSTOMER);
        } catch (Exception e) {
            log.error("Some error occured while checking if the customer already exists, requested customer id to be checked against {}, stacktrace : ", id, e);
            response = new Response(ResponseStatus.ERROR, null,  Constants.ERROR_MESSAGE);
        }
        return response;
    }

    @Override
    public Response isAdmin(Long id) {
        Response response;
        try {
            response = new Response(customerRepository.fetchAdminById(id).isPresent());
        } catch (Exception e) {
           log.error("Some error occured while checking if the provided 'id' is admin, id {}, stacktrace : " , id, e);
        response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
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
        } catch (SQLException e) {
            log.error("Some error occured while deleting the customer with id {}, stacktrace", cid, e);
        return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }

        return new Response("Customer deleted");
    }

    @Override
    public Response fetchAllAdmins() {//fetches all admins excluding super admin and customer.
        List<Customer> admins;
        try {
            admins = customerRepository.getCustomers()
                    .stream()
                    .filter(c -> c.getRole() == Roles.ADMIN)
                    .toList();
            if (admins.isEmpty()) {
                return new Response(null, "No admin found");//throws NoAdminFoundException if no admin found CustomerRepository.
            }
        } catch (SQLException e) {
            log.error("Some error occured while fetching all admins : ", e);
          return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return new Response(admins);//returns list of admins if any found.
    }

}