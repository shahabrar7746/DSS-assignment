package org.assignment.serviceimlementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assignment.entities.User;

import org.assignment.enums.ResponseStatus;
import org.assignment.enums.Roles;

import org.assignment.services.AdminService;

import org.assignment.repository.interfaces.UserRepository;

import org.assignment.util.Constants;
import org.assignment.util.Response;


import java.util.*;

@Slf4j
@AllArgsConstructor
public class AdminServiceImplementation implements AdminService {
    private UserRepository userRepository;

    @Override
    public Response grantAccess(Long id) {
        Optional<User> customer;
        try {
            customer = userRepository.fetchById(id);
            if (customer.isEmpty()) {
                return new Response(ResponseStatus.ERROR, null, "Could not find user with given id");
            }
            if (customer.get().getRole() == Roles.ADMIN) {
                return new Response(ResponseStatus.ERROR, null, "Already a Admin");//indicates if the user is already an admin.
            }
            if (customer.get().getRole() == Roles.SUPER_ADMIN) {//executed if super admins authority is tried to overridden.
                return new Response(ResponseStatus.ERROR, null, "action not allowed");
            }
            customer.ifPresent(c -> c.setRole(Roles.ADMIN));
            userRepository.updateCustomer(customer.get());
        } catch (Exception e) {
            log.error("Some error occured while granting admin access to {}, stacktrace : ", id, e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return new Response(ResponseStatus.SUCCESSFUL, "Access granted !!", null);
    }

    @Override
    public Response revokeAccess(Long id) {
        Response response;
        try {
            Optional<User> customer = userRepository.fetchAdminById(id);
            if (customer.isEmpty()) {
                return new Response(ResponseStatus.ERROR, null, "Could not find user with given id");
            }
            if (customer.get().getRole() == Roles.SUPER_ADMIN) {
                return new Response(ResponseStatus.SUCCESSFUL, null, "Cannot perform action");
            }
            if (customer.get().getRole() == Roles.CUSTOMER) {
                return new Response(ResponseStatus.ERROR, null, "The chosen user is already a user");
            }
            customer.ifPresent(c -> c.setRole(Roles.CUSTOMER));
            userRepository.updateCustomer(customer.get());//critical section.
            response = new Response(ResponseStatus.SUCCESSFUL, "Access revoked", null);
        } catch (Exception e) {
            log.error("Some error occured while revoke admin access from {}, stacktrace : ", id, e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return response;
    }

}