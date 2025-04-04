package org.assignment.serviceimlementation;

import jakarta.persistence.PersistenceException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assignment.entities.*;

import org.assignment.enums.ResponseStatus;
import org.assignment.enums.Roles;

import org.assignment.repository.interfaces.CustomerRepository;

import org.assignment.services.CustomerService;

import org.assignment.util.Constants;
import org.assignment.util.Response;

import java.sql.SQLException;

import java.util.*;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class CustomerServiceImplementation implements CustomerService {
    private final CustomerRepository repository;
    private static final String SUPER_ADMIN_PASSWORD = System.getenv("SUPER_ADMIN_PASSWORD");

    @Override
    public Response getCustomerById(Long id) {
        Optional<Customer> customer;
        try {
            customer = repository.fetchById(id);
        } catch (SQLException e) {
            log.error("Some error occured while retrieving customer based on id : ", e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        if (customer.isEmpty() || customer.get().getRole() != Roles.CUSTOMER) {
            return new Response(null, "No customer found");
        }
        return new Response(customer.get());
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        Optional<Customer> optionalCustomer = Optional.empty();
        try {
            optionalCustomer = repository.fetchByEmail(email);
        } catch (SQLException | PersistenceException e) {
            log.error("Some error occured while finding customer by email, mail {}", email, e);
        }
        return optionalCustomer;
    }

    @Override
    public Response getAllCustomer() {
        List<Customer> allCustomer;
        try {
            allCustomer = repository
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
    public Response deleteCustomer(Long cid) {
        try {
            Optional<Customer> customer = repository.fetchById(cid);//fetch customer by provided id.
            if (customer.isEmpty() || customer.get().getRole() != Roles.CUSTOMER) {
                return new Response(null, "No Customer object found");
            }
            repository.removeCustomer(customer.get());//critical section
        } catch (SQLException e) {
            log.error("Some error occured while deleting the customer with id {}, stacktrace", cid, e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }

        return new Response("Customer deleted");
    }

    @Override
    public Response fetchAllAdmins() {
        List<Customer> admins;
        try {
            admins = repository.getCustomers()
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

    @Override
    public boolean authenticateSuperAdmin(String password) {
        return password.equals(SUPER_ADMIN_PASSWORD);
    }

    @Override
    public Response customerExists(Long id) {
        Response response;
        try {
            Optional<Customer> optionalCustomer = repository.fetchById(id);
            if (optionalCustomer.isEmpty()) {
                return new Response(ResponseStatus.ERROR, null, "Could not find customer with given id");
            }
            response = new Response(optionalCustomer.get().getRole() == Roles.CUSTOMER);
        } catch (Exception e) {
            log.error("Some error occured while checking if the customer already exists, requested customer id to be checked against {}, stacktrace : ", id, e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return response;
    }


    @Override
    public Response isAdmin(Long id) {
        Response response;
        try {
            response = new Response(repository.fetchAdminById(id).isPresent());
        } catch (Exception e) {
            log.error("Some error occured while checking if the provided 'id' is admin, id {}, stacktrace : ", id, e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return response;
    }


}
