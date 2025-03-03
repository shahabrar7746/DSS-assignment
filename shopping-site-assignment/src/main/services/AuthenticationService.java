package main.services;

import main.exceptions.CustomerNotFoundException;
import main.exceptions.TrialLimitExceedException;
import main.exceptions.UserAlreadyExistsException;
import main.serviceimlementation.AuthenticationServiceImplementation;

import java.sql.SQLException;

public interface AuthenticationService {

    /**
     * Log in user either as Customer, Admin or super admin based upon their role.
     *
     * @throws TrialLimitExceedException if try limit exceeds.
     */
    void login() throws TrialLimitExceedException;

    /**
     * Registers user as customer. lets user register their credentials if they do not exist in system.
     * @throws UserAlreadyExistsException if credentials already exist in main.repository.
     */
    void register() throws UserAlreadyExistsException, CustomerNotFoundException, SQLException;
    static AuthenticationService getInstance(){
        return AuthenticationServiceImplementation.getInstance();
    }
}
