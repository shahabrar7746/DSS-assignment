package org.assignment.repositoryhibernateimpl;

import jakarta.persistence.*;
import org.assignment.entities.Customer;
import org.assignment.enums.Roles;
import org.assignment.exceptions.CustomerNotFoundException;
import org.assignment.repository.interfaces.CustomerRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CustomerRepoHibernateImpl implements CustomerRepository {
    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory("myPersistenceUnit");
    private final EntityManager manager = factory.createEntityManager();
    private final EntityTransaction transaction = manager.getTransaction();
    private final String BASE_SELECTION_QUERY = " SELECT c FROM Customer c ";

    @Override
    public List<Customer> getCustomers() throws CustomerNotFoundException, SQLException {

        TypedQuery<Customer> query = manager.createQuery(BASE_SELECTION_QUERY, Customer.class);
        return query.getResultList();
    }

    @Override
    public Optional<Customer> fetchById(Long id) throws CustomerNotFoundException, SQLException {
        Customer data = manager.find(Customer.class, id);
        return data == null ? Optional.empty() : Optional.of(data);
    }

    @Override
    public Optional<Customer> fetchAdminById(Long id) throws SQLException {
        String jpql = BASE_SELECTION_QUERY + " WHERE c.id =:id AND c.role =:role";
        TypedQuery<Customer> query = manager.createQuery(jpql, Customer.class);
        query.setParameter("id", id);
        query.setParameter("role", Roles.ADMIN);
        Customer data = query.getSingleResultOrNull();
        return data == null ? Optional.empty() : Optional.of(data);
    }

    @Override
    public Optional<Customer> fetchByEmail(String email) throws SQLException {
        String jpql = BASE_SELECTION_QUERY + " WHERE c.email = :email";
        TypedQuery<Customer> query = manager.createQuery(jpql, Customer.class);
        query.setParameter("email", email);
        Customer data = query.getSingleResultOrNull();
        return data == null ? Optional.empty() : Optional.of(data);
    }


    @Override
    public Customer addCustomer(Customer customer) throws SQLException {
        try {
            transaction.begin();
            customer.setRole(Roles.CUSTOMER);
            manager.persist(customer);
            transaction.commit();
        } catch (EntityExistsException e) {
            throw new RuntimeException("Customer already exist");
        }
        return customer;
    }

    @Override
    public Customer updateCustomer(Customer customer) throws SQLException {
        transaction.begin();
        manager.merge(customer);
        transaction.commit();
        return customer;
    }

    @Override
    public void removeCustomer(Customer customer) {
        transaction.begin();
        manager.remove(customer);
        transaction.commit();
    }
}
