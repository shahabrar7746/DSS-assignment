package org.assignment.services;

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

     Response getCustomerById();
     /**
      * Fetches all customer from main.repository excluding customer with admin authority.
      *
      * @return List of Customer from main.repository excluding admins and super admins.
      * @throws CustomerNotFoundException if no customer object is found inside repositry.
      * @see #getCustomerById()
      */
     Response getAllCustomer();
     /**
      * Fetches all Products from main.repository.
      *
      * @return List of available Products.
      * @throws NoProductFoundException if  product main.repository is empty.
      * @see #getProductsByType()
      */
     Response getAllProdcuts();
     /**
      * Fetches all products based on provided type from main.repository.
      *
      * @return List of Products of same type as provided by admin.
      * @throws OperationNotSupportedException
      */
     Response getProductsByType();

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
     Response grantAccess(boolean isAuthorized);
     /**
      * Revokes admin access for any customer. must be performed by Super admin.
      *
      * @return
      * @throws UnauthorizedOperationException if the user is not super admin.
      * @throws NoAdminFoundException          if no admin found in main.repository.
      * @throws TrialLimitExceedException      try limit exceeds.
      * @see #grantAccess(boolean)
      */
     Response revokeAccess(boolean isAuthorized);
     /**
      * Cancels order placed by customer.
      *
      * @param isAuthorized to check if the user is super admin or not.
      * @return
      * @throws OrderNotFoundException         if no order is found for the id or order is empty.
      * @throws UnauthorizedOperationException if not authorized or called by any other entity except super admin.
      */
     Response cancelOrder(boolean isAuthorized);
     /**
      * @param isAuthorized validation to check if the operation performed by super admin.
      *                     if not validated, throws exception.
      * @return
      * @throws UnauthorizedOperationException if performed by super admin.
      * @throws CustomerNotFoundException      if the customer id is incorrect or customer main.repository is empty.
      */
     Response deleteCustomer(boolean isAuthorized) ;
     /**
      * Used to fetch all admins from main.repository.
      *
      * @return List of Customer if any customer is found with admin authority.
      * @throws NoAdminFoundException if no customer is found with admin authority.
      */
     Response fetchAllAdmins();

     static AdminService getInstance(){
          return AdminServiceImplementation.getInstance();
     }
}
