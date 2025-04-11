package org.assignment.repository.interfaces;

import jakarta.persistence.PersistenceException;
import org.assignment.entities.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    /**
     * Used to get all customers including customers with admin authority.
     * @return List of all registered customers
     */
     List<User> getCustomers() throws SQLException, PersistenceException;
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
    Optional<User> fetchAdminById(Long id) throws SQLException, PersistenceException;
    /**
     * returns user corresponding to provided email.
     * @param email
     * @return
     */
    Optional<User> fetchByEmail(String email) throws SQLException, PersistenceException;


    User addCustomer(User user) throws SQLException, PersistenceException;

    User updateCustomer(User user) throws PersistenceException, SQLException;

    void removeCustomer(User user) throws  PersistenceException;

    Optional<User> fetchSellerById(Long id);

    List<User> fetchAllSellers();
}
