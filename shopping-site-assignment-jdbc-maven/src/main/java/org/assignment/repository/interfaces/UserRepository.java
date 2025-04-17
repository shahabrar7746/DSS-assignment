package org.assignment.repository.interfaces;

import jakarta.persistence.PersistenceException;
import org.assignment.entities.User;
import org.assignment.enums.Roles;
import org.assignment.wrappers.CustomerWrapper;
import org.assignment.wrappers.SellerWrapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    /**
     * Used to get all customers including customers with admin authority.
     * @return List of all registered customers
     */

      /**
     * Fetches User on the basis of id. converts list to map for efficient search operation.
     * @param id id to search.
     * @return Optional of User if data found or else empty Optional.
     */
     Optional<User> fetchById(Long id)  throws  SQLException, PersistenceException;
    /**
     * Searches for admin based on the id.
     * @param id id to be searched.
     * @return returns Optional of user if any admin with the same id is found or else false.
     */

    /**
     * returns user corresponding to provided email.
     * @param email
     * @return
     */
    Optional<User> fetchByEmail(String email) throws SQLException, PersistenceException;


    User addCustomer(User user) throws SQLException, PersistenceException;

    User updateCustomer(User user) throws PersistenceException, SQLException;

    Optional<User> fetchUserByIdAndRole(Long id, Roles role) throws SQLException;
    List<User> fetchUserByRole(Roles role) throws SQLException;
    public List<SellerWrapper> fetchSellers();
    List<CustomerWrapper> fetchCustomerWrappers();
}
