package org.assignment.serviceimlementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assignment.entities.Customer;

import org.assignment.enums.ResponseStatus;
import org.assignment.enums.Roles;

import org.assignment.services.AdminService;

import org.assignment.repository.interfaces.CustomerRepository;

import org.assignment.util.Constants;
import org.assignment.util.Response;


import java.util.*;

@Slf4j
@AllArgsConstructor
public class AdminServiceImplementation implements AdminService {
    private CustomerRepository customerRepository;

    @Override
    public Response grantAccess(Long id) {
        Optional<Customer> customer;
        try {
            customer = customerRepository.fetchById(id);
            if (customer.isEmpty()) {
                return new Response(ResponseStatus.ERROR, null, "Could not find customer with given id");
            }
            if (customer.get().getRole() == Roles.ADMIN) {
                return new Response(null, "Already a Admin");//indicates if the customer is already a admin.
            }
            if (customer.get().getRole() == Roles.SUPER_ADMIN) {//executed if super admins authority is tried to overridden.
                return new Response(null, "action not allowed");
            }
            customer.ifPresent(c -> c.setRole(Roles.ADMIN));
            customerRepository.updateCustomer(customer.get());
        } catch (Exception e) {
            log.error("Some error occured while granting admin access to {}, stacktrace : ", id, e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return new Response("Access granted !!");
    }

    @Override
    public Response revokeAccess(Long id) {
        Response response;
        try {
            Optional<Customer> customer = customerRepository.fetchAdminById(id);
            if (customer.isEmpty()) {
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
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return response;
    }

}