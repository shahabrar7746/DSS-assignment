package org.assignment.repositoryhibernateimpl;

import jakarta.persistence.*;
import org.assignment.entities.Product;
import org.assignment.exceptions.NoProductFoundException;
import org.assignment.repository.interfaces.ProductRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductRepoHibernateImpl implements ProductRepository {
    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory("myPersistenceUnit");
    private final EntityManager manager = factory.createEntityManager();
    private final String BASE_SELECTION_QUERY = " SELECT p FROM Product p ";

    @Override
    public List<Product> fetchProducts() throws NoProductFoundException, SQLException {
        TypedQuery<Product> query = manager.createQuery(BASE_SELECTION_QUERY, Product.class);
        return query.getResultList();
    }

    @Override
    public Optional<Product> fetchProductById(Long id) throws SQLException, NoProductFoundException {
        Product product = manager.find(Product.class, id);
        return product == null ? Optional.empty() : Optional.of(product);
    }

    @Override
    public Optional<Product> fetchProductByName(String name) throws SQLException, NoProductFoundException {
        String jpql = BASE_SELECTION_QUERY + " WHERE p.name =:name";
        TypedQuery<Product> query = manager.createQuery(jpql, Product.class);
        query.setParameter("name", name);
        Product product = query.getSingleResultOrNull();
        return product == null ? Optional.empty() : Optional.of(product);
    }
}
