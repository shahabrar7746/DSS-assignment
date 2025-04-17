package org.assignment.services;

import org.assignment.entities.User;
import org.assignment.util.Response;

public interface UserService {

    Response getAllMaskedCustomer();

    Response findByEmail(String email);

    Response updateCustomerAndCart(User user);

    Response getAllCustomer();

    Response fetchAllAdmins();

    boolean authenticateSuperAdmin(String password);

    Response customerExists(Long id);

    Response isAdmin(Long id);

    Response fetchAllSellers();
}
