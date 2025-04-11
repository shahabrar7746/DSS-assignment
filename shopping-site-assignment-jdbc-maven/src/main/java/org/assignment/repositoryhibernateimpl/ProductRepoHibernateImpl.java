package org.assignment.repositoryhibernateimpl;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.assignment.entities.Product;

import org.assignment.repository.interfaces.ProductRepository;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
public class ProductRepoHibernateImpl implements ProductRepository {

    private final   EntityManager manager;
    private final   EntityTransaction transaction;
    private static final String BASE_SELECTION_QUERY = " SELECT p FROM Product p ";

    @Override
    public List<Product> fetchProducts() {
        TypedQuery<Product> query = manager.createQuery(BASE_SELECTION_QUERY, Product.class);
        return query.getResultList();
    }

    @Override
    public Product updateProduct(Product product) {
        transaction.begin();
        manager.merge(product);
        transaction.commit();
        return product;
    }

    @Override
    public Optional<Product> fetchProductById(Long id) {
        Product product = manager.find(Product.class, id);
        return product == null ? Optional.empty() : Optional.of(product);
    }

    @Override
    public Optional<Product> fetchProductByName(String name) {
        String jpql = BASE_SELECTION_QUERY + " WHERE p.name =:name";
        TypedQuery<Product> query = manager.createQuery(jpql, Product.class);
        query.setParameter("name", name);
        return Optional.ofNullable(query.getSingleResultOrNull());
    }

    @Override
    public void addProduct(Product product) {
        transaction.begin();
        manager.persist(product);
        transaction.commit();
    }
}
