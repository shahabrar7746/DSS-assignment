package org.assignment.services;

import org.assignment.entities.Product;
import org.assignment.enums.ProductType;
import org.assignment.exceptions.*;
import org.assignment.serviceimlementation.AdminServiceImplementation;
import org.assignment.util.Response;

import javax.naming.OperationNotSupportedException;


public interface AdminService {
    /**
     * Searches for the customer based on its id.
     *
     * @return Customer object of provided id.
     * @throws CustomerNotFoundException if no customer is found or customer is empty.
     * @see #getAllCustomer()
     */

    Response getCustomerById(Long id);


    Response getAllProdcuts();

    Response getAllCustomer();

    /**
     * Fetches all Products from main.repository.
     * <p>
     * <p>
     * Response getAllProdcuts();
     * /**
     * Fetches all products based on provided type from main.repository.
     *
     * @return List of Products of same type as provided by admin.
     * @throws OperationNotSupportedException
     */
    Response getProductsByType(ProductType type);

    /**
     * Fetches all delivered orders from main.repository.
     *
     * @return List of all delivered orders.
     * @throws OrderNotFoundException if no delivered orders is found.
     */
    Response getAllDeliveredOrders();


    Response grantAccess(Long id);


    Response revokeAccess(Long id);



    Response deleteCustomer(Long cid);

    /**
     * Used to fetch all admins from main.repository.
     *
     * @return List of Customer if any customer is found with admin authority.
     * @throws NoAdminFoundException if no customer is found with admin authority.
     */
    Response fetchAllAdmins();

    boolean authenticateSuperAdmin(String password);

    Response customerExists(Long id);
    Response isAdmin(Long id);


}
