package services;

import entities.Customer;
import entities.Order;
import entities.Product;

import exceptions.*;
import serviceimlementation.AdminServiceImplementation;

import javax.naming.OperationNotSupportedException;
import java.util.List;


public interface AdminService {
     /**
      * Searches for the customer based on its id.
      * @return Customer object of provided id.
      * @throws  CustomerNotFoundException if no customer is found or customer is empty.
      * @see #getAllCustomer()
      */

     Customer getCustomerById();
     /**
      * Fetches all customer from repository excluding customer with admin authority.
      * @return List of Customer from repository excluding admins and super admins.
      * @throws CustomerNotFoundException if no customer object is found inside repositry.
      * @see #getCustomerById()
      */
     List<Customer> getAllCustomer();
     /**
      * Fetches all Products from repository.
      * @return List of available Products.
      * @throws NoProductFoundException if  product repository is empty.
      * @see #getProductsByType()
      */
     List<Product> getAllProdcuts();
     /**
      * Fetches all products based on provided type from repository.
      * @return List of Products of same type as provided by admin.
      * @throws OperationNotSupportedException
      */
     List<Product> getProductsByType() throws OperationNotSupportedException;

     /** Fetches all delivered orders from repository.
      * @return List of all delivered orders.
      * @throws OrderNotFoundException if no delivered orders is found.
      */
     List<Order> getAllDeliveredOrders();
     /**
      * Grants admin access to a regular customer, only called and used  by Super admin.
      * @param isAuthorized provided and called by super admin.
      * @throws OperationNotSupportedException if the operation is not performed by super admin.
      * @throws TrialLimitExceedException if the try limit exceeds.
      * @see #revokeAccess(boolean)
      */
     void grantAccess(boolean isAuthorized) throws OperationNotSupportedException;
     /**
      Revokes admin access for any customer. must be performed by Super admin.
      @throws UnauthorizedOperationException if the user is not super admin.
      @throws  NoAdminFoundException if no admin found in repository.
      @throws  TrialLimitExceedException try limit exceeds.
      @see #grantAccess(boolean)
      */
     void revokeAccess(boolean isAuthorized) throws OperationNotSupportedException;
     /** Cancels order placed by customer.
      * @param isAuthorized to check if the user is super admin or not.
      * @throws  OrderNotFoundException if no order is found for the id or order is empty.
      * @throws UnauthorizedOperationException if not authorized or called by any other entity except super admin.
      */
     void cancelOrder(boolean isAuthorized) throws OperationNotSupportedException;
     /**
      *
      * @param isAuthorized validation to check if the operation performed by super admin.
      *                     if not validated, throws exception.
      * @throws UnauthorizedOperationException if performed by super admin.
      * @throws CustomerNotFoundException if the customer id is incorrect or customer repository is empty.
      *
      */
     void deleteCustomer(boolean isAuthorized) throws OperationNotSupportedException;
     /**
      * Used to fetch all admins from repository.
      * @return List of Customer if any customer is found with admin authority.
      * @throws NoAdminFoundException if no customer is found with admin authority.
      */
     List<Customer> fetchAllAdmins();

     AdminService getAdminInstance();
}
