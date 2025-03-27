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

    /**
     * Grants admin access to a regular customer, only called and used  by Super admin.
     *
     * @param isAuthorized provided and called by super admin.
     * @return
     * @throws OperationNotSupportedException if the operation is not performed by super admin.
     * @throws TrialLimitExceedException      if the try limit exceeds.
     * @see #revokeAccess(boolean)
     */
    Response grantAccess(Long id);

    /**
     * Revokes admin access for any customer. must be performed by Super admin.
     *
     * @return
     * @throws UnauthorizedOperationException if the user is not super admin.
     * @throws NoAdminFoundException          if no admin found in main.repository.
     * @throws TrialLimitExceedException      try limit exceeds.
     * @see #grantAccess(boolean)
     */
    Response revokeAccess(Long id);

    /**
     * Cancels order placed by customer.
     *
     * @param isAuthorized to check if the user is super admin or not.
     * @return
     * @throws OrderNotFoundException         if no order is found for the id or order is empty.
     * @throws UnauthorizedOperationException if not authorized or called by any other entity except super admin.
     */

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

    static AdminService getInstance() {
        return AdminServiceImplementation.getInstance();
    }
}
