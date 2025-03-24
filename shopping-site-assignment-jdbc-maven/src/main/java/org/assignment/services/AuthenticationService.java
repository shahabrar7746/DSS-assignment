package org.assignment.services;

import org.assignment.exceptions.CustomerNotFoundException;
import org.assignment.exceptions.TrialLimitExceedException;
import org.assignment.exceptions.UserAlreadyExistsException;
import org.assignment.serviceimlementation.AuthenticationServiceImplementation;

import java.sql.SQLException;

public interface AuthenticationService {

    /**
     * Log in user either as Customer, Admin or super admin based upon their role.
     *
     * @throws TrialLimitExceedException if try limit exceeds.
     */
    void login() throws TrialLimitExceedException, SQLException;

    /**
     * Registers user as customer. lets user register their credentials if they do not exist in system.
     * @throws UserAlreadyExistsException if credentials already exist in main.repository.
     */
    void register() throws UserAlreadyExistsException, CustomerNotFoundException, SQLException;
    static AuthenticationService getInstance(){
        return AuthenticationServiceImplementation.getInstance();
    }
}
