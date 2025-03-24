package org.assignment.repository.interfaces;

import jakarta.persistence.PersistenceException;
import org.assignment.entities.Customer;
import org.assignment.exceptions.CustomerNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    /**
     * Used to get all customers including customers with admin authority.
     * @return List of all registered customers
     */
     List<Customer> getCustomers() throws CustomerNotFoundException, SQLException, PersistenceException;
      /**
     * Fetches Customer on the basis of id. converts list to map for efficient search operation.
     * @param id id to search.
     * @return Optional of Customer if data found or else empty Optional.
     */
     Optional<Customer> fetchById(Long id) throws CustomerNotFoundException, SQLException, PersistenceException;
    /**
     * Searches for admin based on the id.
     * @param id id to be searched.
     * @return returns Optional of customer if any admin with the same id is found or else false.
     */
    Optional<Customer> fetchAdminById(Long id) throws SQLException, PersistenceException;
    /**
     * returns customer corresponding to provided email.
     * @param email
     * @return
     */
    Optional<Customer> fetchByEmail(String email) throws SQLException, PersistenceException;


    Customer addCustomer(Customer customer) throws SQLException, PersistenceException;

    Customer updateCustomer(Customer customer) throws SQLException, PersistenceException;

    void removeCustomer(Customer customer) throws  PersistenceException;
}
