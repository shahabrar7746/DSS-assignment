package org.assignment.repositoryhibernateimpl;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.assignment.entities.User;
import org.assignment.enums.Roles;

import org.assignment.repository.interfaces.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepoHibernateImpl implements UserRepository {

    private final EntityManager manager;
    private final EntityTransaction transaction;
    private static final String BASE_SELECTION_QUERY = " SELECT c FROM User c ";


    @Override
    public List<User> getCustomers() {
        TypedQuery<User> query = manager.createQuery(BASE_SELECTION_QUERY, User.class);
        return query.getResultList();
    }

    @Override
    public Optional<User> fetchById(Long id) {
        return Optional.ofNullable(manager.find(User.class, id));
    }

    @Override
    public Optional<User> fetchAdminById(Long id) {
        String jpql = BASE_SELECTION_QUERY + " WHERE c.id =:id AND c.role =:role";
        TypedQuery<User> query = manager.createQuery(jpql, User.class);
        query.setParameter("id", id);
        query.setParameter("role", Roles.ADMIN);
        User data = query.getSingleResultOrNull();
        return Optional.ofNullable(data);
    }

    @Override
    public Optional<User> fetchByEmail(String email) {
        String jpql = BASE_SELECTION_QUERY + " WHERE c.email = :email";
        TypedQuery<User> query = manager.createQuery(jpql, User.class);
        query.setParameter("email", email);

        User data = query.getSingleResultOrNull();
        if (data != null) {
            manager.detach(data);
        }
        return Optional.ofNullable(data);
    }

    @Override
    public User addCustomer(User user) {
        try {
            user.setCart(new ArrayList<>());
            user.setRole(Roles.CUSTOMER);
            transaction.begin();
            manager.persist(user);

        } catch (EntityExistsException e) {
            throw new RuntimeException("User already exist");
        } finally {
            transaction.commit();
        }
        return user;
    }

    @Override
    public User updateCustomer(User user) {
        transaction.begin();
        manager.merge(user);
        transaction.commit();
        return user;
    }

    @Override
    public void removeCustomer(User user) {
        transaction.begin();
        manager.remove(user);
        transaction.commit();
    }

    @Override
    public Optional<User> fetchSellerById(Long id) {
        String jpql = BASE_SELECTION_QUERY + " WHERE c.id = :id AND c.role = :role";
        TypedQuery<User> typedQuery = manager.createQuery(jpql, User.class);
        typedQuery.setParameter("id", id);
        typedQuery.setParameter("role", Roles.SELLER);
        return Optional.ofNullable(typedQuery.getSingleResultOrNull());
    }

    @Override
    public List<User> fetchAllSellers() {
        String jpql = BASE_SELECTION_QUERY + " WHERE c.role = :role";
        TypedQuery<User> typedQuery = manager.createQuery(jpql, User.class);
        typedQuery.setParameter("role", Roles.SELLER);
        return typedQuery.getResultList();
    }
}
