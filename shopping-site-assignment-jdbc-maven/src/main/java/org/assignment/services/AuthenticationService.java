package org.assignment.services;

import org.assignment.exceptions.UserAlreadyExistsException;
import org.assignment.util.Response;

public interface AuthenticationService {
    Response login(String email, String password);

    Response save(String email, String password, String address, String name);

}
