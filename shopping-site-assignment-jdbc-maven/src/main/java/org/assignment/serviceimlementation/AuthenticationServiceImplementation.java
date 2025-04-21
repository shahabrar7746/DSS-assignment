package org.assignment.serviceimlementation;

import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import org.assignment.entities.User;

import org.assignment.enums.ResponseStatus;
import org.assignment.enums.Roles;

import org.assignment.repository.interfaces.UserRepository;

import org.assignment.services.*;

import org.assignment.ui.AdminUI;
import org.assignment.ui.CustomerUI;

import org.assignment.ui.UI;
import org.assignment.util.Constants;
import org.assignment.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;

import java.util.Optional;
import java.util.Scanner;

@AllArgsConstructor
public class AuthenticationServiceImplementation implements AuthenticationService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final UserService userService;
    private final AdminService adminService;
    private final ProductService productService;
    private final OrderService orderService;
    private final CartService cartService;
    private final Scanner sc;

    @Override
    public Response login(String email, String pasword) {
        Response response;
        User c;
        try {
            Optional<User> customer = userRepository.fetchByEmail(email);
            if(customer.isPresent() && customer.get().getPassword().equals(pasword))
            {
                c = customer.get();
                c.setLoggedIn(true);
                userService.updateCustomerAndCart(c);
                if (c.getRole() == Roles.ADMIN || c.getRole() == Roles.SUPER_ADMIN) {
                    UI adminUI = new AdminUI(sc,adminService, userService, productService, orderService);
                    adminUI.initAdminServices(c);
                } else if (c.getRole() == Roles.CUSTOMER) {
                    UI customerUI = new CustomerUI(sc, orderService, productService, cartService);
                    customerUI.initCustomerServices(c);
                }
                c.setLoggedIn(false);
                userService.updateCustomerAndCart(c);
                response = new Response(ResponseStatus.SUCCESSFUL, "Logged out !!", null);
            }
            else  {
                response = new Response(ResponseStatus.ERROR, null, "Invalid Credentials...");
            }
        } catch (Exception e) {
            log.error("Some error occured while log in for email {} and password {} ", email, pasword, e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return response;
    }

    @Override
    public Response save(String email, String password, String address, String name) {
        Response response = null;
        try {
            Optional<User> optionalCustomer = userRepository.fetchByEmail(email);
            if (optionalCustomer.isPresent()) {
                response = new Response(ResponseStatus.ERROR, null, "User already exists");
            }
            if (response == null) {
                User newUser = User.builder()
                        .role(Roles.CUSTOMER)
                        .address(address)
                        .email(email.toUpperCase())
                        .name(name)
                        .password(password)
                        .build();
                userRepository.addCustomer(newUser);
                response = new Response(ResponseStatus.SUCCESSFUL, newUser, null);
            }
        } catch (SQLException | PersistenceException e) {
            log.error("Some error occured while registering user with email {} and password {} ", email, password, e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return response;
    }
}
