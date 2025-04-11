package org.assignment.serviceimlementation;

import jakarta.persistence.PersistenceException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assignment.entities.*;

import org.assignment.enums.ResponseStatus;
import org.assignment.enums.Roles;

import org.assignment.repository.interfaces.UserRepository;

import org.assignment.services.UserService;

import org.assignment.util.Constants;
import org.assignment.util.Response;
import org.assignment.wrappers.CustomerWrapper;
import org.assignment.wrappers.SellerWrapper;

import java.sql.SQLException;

import java.util.*;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserRepository repository;
    private static final String SUPER_ADMIN_PASSWORD = System.getenv("SUPER_ADMIN_PASSWORD");

    @Override
    public Response getAllMaskedCustomer() {
        Response response;
        try {
            List<CustomerWrapper> maskedCustomer = new ArrayList<>();
            repository
                    .getCustomers()
                    .stream()
                    .filter(c -> c.getRole() == Roles.CUSTOMER)
                    .forEach(c -> maskedCustomer.add(new CustomerWrapper(c.getName(), c.getEmail())));

            response = new Response(ResponseStatus.SUCCESSFUL, maskedCustomer, null);

        } catch (Exception e) {
            log.error("Some error occurred while retrieving all customers for masking : ", e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return response;
    }


    @Override
    public Response findByEmail(String email) {
        Response response;
        Optional<User> optionalCustomer;
        try {
            optionalCustomer = repository.fetchByEmail(email);

            response = new Response(ResponseStatus.SUCCESSFUL, optionalCustomer, null);
        } catch (SQLException | PersistenceException e) {
            log.error("Some error occurred while finding user by email, mail {}", email, e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return response;
    }

    @Override
    public Response updateCustomerAndCart(User user) {
        Response response;
        try {
            user.getCart()
                    .forEach(cartItems -> cartItems.setUser(user));
            repository.updateCustomer(user);
            response = new Response(ResponseStatus.SUCCESSFUL, "User updated", null);
        } catch (Exception e) {
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
            log.error("Some error occured while updating user and cart", e);
        }
        return response;
    }

    @Override
    public Response getAllCustomer() {
        List<User> allUser;
        try {
            allUser = repository
                    .getCustomers()
                    .stream()
                    .filter(c -> c.getRole() == Roles.CUSTOMER)
                    .toList();
        } catch (Exception ex) {
            log.error("error while fetching user records: ", ex);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        if (allUser.isEmpty()) {
            return new Response(ResponseStatus.ERROR, null, "User main repository is empty"); //executed if no user object is found
        }
        return new Response(ResponseStatus.SUCCESSFUL, allUser, null);// returns all the user.
    }

    @Override
    public Response fetchAllAdmins() {
        List<User> admins;
        try {
            admins = repository.getCustomers()
                    .stream()
                    .filter(c -> c.getRole() == Roles.ADMIN)
                    .toList();
            if (admins.isEmpty()) {
                return new Response(ResponseStatus.ERROR, null, "No admin found");//throws NoAdminFoundException if no admin found UserRepository.
            }
        } catch (SQLException e) {
            log.error("Some error occured while fetching all admins : ", e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return new Response(ResponseStatus.SUCCESSFUL, admins, null);//returns list of admins if any found.
    }

    @Override
    public boolean authenticateSuperAdmin(String password) {
        return password.equals(SUPER_ADMIN_PASSWORD);
    }

    @Override
    public Response customerExists(Long id) {
        Response response;
        try {
            Optional<User> optionalCustomer = repository.fetchById(id);
            if (optionalCustomer.isEmpty()) {
                return new Response(ResponseStatus.ERROR, null, "Could not find user with given id");
            }
            response = new Response(ResponseStatus.SUCCESSFUL, optionalCustomer.get().getRole() == Roles.CUSTOMER, null);
        } catch (Exception e) {
            log.error("Some error occured while checking if the user already exists, requested user id to be checked against {}, stacktrace : ", id, e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return response;
    }


    @Override
    public Response isAdmin(Long id) {
        Response response;
        try {
            response = new Response(ResponseStatus.SUCCESSFUL, repository.fetchAdminById(id).isPresent(), null);
        } catch (Exception e) {
            log.error("Some error occured while checking if the provided 'id' is admin, id {}, stacktrace : ", id, e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return response;
    }

    @Override
    public Response fetchAllSellers() {
        Response response;
        try {
            List<SellerWrapper> wrappers = new ArrayList<>();
            repository.fetchAllSellers()
                    .forEach(seller -> {
                        wrappers.add(new SellerWrapper(seller.getId(), seller.getName()));
                    });
            response = new Response(ResponseStatus.SUCCESSFUL, wrappers, null);
        } catch (Exception e) {
            log.error("Error occured while fetching all sellers ", e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return response;
    }


}
