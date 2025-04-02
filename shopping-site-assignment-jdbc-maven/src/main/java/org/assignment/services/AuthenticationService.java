package org.assignment.services;

import org.assignment.exceptions.UserAlreadyExistsException;
import org.assignment.util.Response;

public interface AuthenticationService {

    /**
     * Log in user either as Customer, Admin or super admin based upon their role.
     *
     * @throws TrialLimitExceedException if try limit exceeds.
     */
    Response login(String email, String password) ;
    Response save(String email, String password, String address, String name);
    /**
     * Registers user as customer. lets user register their credentials if they do not exist in system.
     * @throws UserAlreadyExistsException if credentials already exist in main.repository.
     */


}
