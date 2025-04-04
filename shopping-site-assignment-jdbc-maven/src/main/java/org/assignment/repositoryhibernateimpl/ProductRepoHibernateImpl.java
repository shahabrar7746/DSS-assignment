package org.assignment.repositoryhibernateimpl;

import jakarta.persistence.*;
import jakarta.persistence.spi.PersistenceUnitInfo;
import org.assignment.entities.Product;

import org.assignment.repository.interfaces.ProductRepository;
import org.assignment.util.ConnectionUtility;

import java.util.List;
import java.util.Optional;

public class ProductRepoHibernateImpl implements ProductRepository {

    private EntityManager manager = ConnectionUtility.getEntityManager();
    private static final String BASE_SELECTION_QUERY = " SELECT p FROM Product p ";

    @Override
    public List<Product> fetchProducts() {
        TypedQuery<Product> query = manager.createQuery(BASE_SELECTION_QUERY, Product.class);
        return query.getResultList();
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
        Product product = query.getSingleResultOrNull();
        return product == null ? Optional.empty() : Optional.of(product);
    }
}
