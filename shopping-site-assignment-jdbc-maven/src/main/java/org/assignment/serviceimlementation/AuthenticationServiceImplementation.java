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

@AllArgsConstructor
public class AuthenticationServiceImplementation implements AuthenticationService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final UserService userService;
    private final AdminService adminService;
    private final ProductService productService;
    private final OrderService orderService;
    private final CartService cartService;

    @Override
    public Response login(String email, String pasword) {
        Response response;
        User c;
        try {
            Optional<User> customer = userRepository.fetchByEmail(email);
            if(customer.isEmpty())
            {
                response = new Response(ResponseStatus.ERROR, null, "Invalid Credentials...");
            }

           else if ( customer.get().getEmail().equalsIgnoreCase(email) && customer.get().getPassword().equals(pasword)) {

                c = customer.get();
                c.setLoggedIn(true);
                userService.updateCustomerAndCart(c);
                if (c.getRole() == Roles.ADMIN || c.getRole() == Roles.SUPER_ADMIN) {
                    UI adminUI = new AdminUI(adminService, userService, productService, orderService);
                    adminUI.initAdminServices(c);
                } else if (c.getRole() == Roles.CUSTOMER) {
                    UI customerUI = new CustomerUI(orderService, productService, cartService);
                    customerUI.initCustomerServices(c);
                }
                c.setLoggedIn(false);
                userService.updateCustomerAndCart(c);
                response = new Response(ResponseStatus.SUCCESSFUL, "Loggin out", null);
            }  else  {
              response = new Response(ResponseStatus.ERROR, null, "Could not log in");
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
                        .email(email)
                        .name(name)
                        .password(password)
                        .registeredOn(LocalDateTime.now())
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
