package org.assignment.repositoryhibernateimpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.assignment.entities.OrderedProduct;
import org.assignment.repository.interfaces.OrderedProductRepository;
@RequiredArgsConstructor
public class OrderedProductHibernateImpl implements OrderedProductRepository {
    private final EntityManager manager;
    private final EntityTransaction transaction;
    @Override
    public OrderedProduct save(OrderedProduct product) {
        transaction.begin();
        manager.persist(product);
        transaction.commit();
        return product;
    }

    @Override
    public OrderedProduct update(OrderedProduct product) {
        transaction.begin();
        manager.merge(product);
        transaction.commit();
        return product;
    }
}
