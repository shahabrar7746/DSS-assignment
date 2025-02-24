package services;

import exceptions.TrialLimitExceedException;
import exceptions.UserAlreadyExistsException;

public interface AuthenticationService {

    /**
     * Log in user either as Customer, Admin or super admin based upon their role.
     * @throws TrialLimitExceedException if try limit exceeds.
     *
     */
    void login();

    /**
     * Registers user as customer. lets user register their credentials if they do not exist in system.
     * @throws UserAlreadyExistsException if credentials already exist in repository.
     */
    void register();
}
