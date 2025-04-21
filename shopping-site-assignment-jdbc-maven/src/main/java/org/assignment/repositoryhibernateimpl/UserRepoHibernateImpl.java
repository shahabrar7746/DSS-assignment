package org.assignment.repositoryhibernateimpl;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.assignment.entities.User;
import org.assignment.enums.Roles;

import org.assignment.repository.interfaces.UserRepository;
import org.assignment.wrappers.CustomerWrapper;
import org.assignment.wrappers.SellerWrapper;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepoHibernateImpl implements UserRepository {
    @Override
    public List<CustomerWrapper> fetchCustomerWrappers() {
        String jpql = "SELECT NEW  org.assignment.wrappers.CustomerWrapper(u.email, u.name) FROM User u " + " WHERE u.role = :role";
        TypedQuery<CustomerWrapper> query = manager.createQuery(jpql, CustomerWrapper.class);
        query.setParameter("role", Roles.CUSTOMER);
        return query.getResultList();
    }

    private final EntityManager manager;
    private final EntityTransaction transaction;
    private static final String BASE_SELECTION_QUERY = " SELECT u FROM User u ";



    @Override
    public Optional<User> fetchById(Long id) {
        return Optional.ofNullable(manager.find(User.class, id));
    }



    @Override
    public Optional<User> fetchByEmail(String email) {
        String jpql = BASE_SELECTION_QUERY + " WHERE u.email = :email";
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
            transaction.commit();
        } catch (EntityExistsException e) {
            transaction.setRollbackOnly();
            throw new RuntimeException("User already exist");
        }
        return user;
    }

    @Override
    public User updateCustomer(User user) {
        try {
            transaction.begin();
            manager.merge(user);
            transaction.commit();
        }catch (Exception e) {
            transaction.setRollbackOnly();
            throw new RuntimeException(e);
        }
        return user;
    }


    @Override
    public Optional<User> fetchUserByIdAndRole(Long id, Roles role) {
        String jpql = BASE_SELECTION_QUERY + " WHERE u.id = :id AND u.role = :role";
        TypedQuery<User> typedQuery = manager.createQuery(jpql, User.class);
        typedQuery.setParameter("id", id);
        typedQuery.setParameter("role", role);
        return Optional.ofNullable(typedQuery.getSingleResultOrNull());
    }

    @Override
    public List<User> fetchUserByRole(Roles role) {
        String jpql  = BASE_SELECTION_QUERY + "WHERE u.role = :role";
        TypedQuery<User> query = manager.createQuery(jpql, User.class);
        query.setParameter("role", role);
        return query.getResultList();
    }

    @Override
    public List<SellerWrapper> fetchSellers() {
        String jpql = "SELECT NEW  org.assignment.wrappers.SellerWrapper(u.id, u.name) FROM User u " + " WHERE u.role = :role";
        TypedQuery<SellerWrapper> query = manager.createQuery(jpql, SellerWrapper.class);
        query.setParameter("role", Roles.SELLER);
        return query.getResultList();
    }

}
