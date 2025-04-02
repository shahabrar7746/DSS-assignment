package org.assignment.repositoryhibernateimpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.assignment.entities.Seller;
import org.assignment.repository.interfaces.SellerRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class SellerRepoHibernateImpl implements SellerRepository {
    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory("myPersistenceUnit");
    private final EntityManager manager = factory.createEntityManager();
   private static final String BASE_SELECTION_QUERY = " SELECT s FROM Seller s ";
    @Override
    public List<Seller> fetchSellers() throws SQLException {
        TypedQuery<Seller> query = manager.createQuery(BASE_SELECTION_QUERY, Seller.class);
        return query.getResultList();
    }

    @Override
    public Optional<Seller> fetchById(Long id) throws SQLException {
      Seller seller = manager.find(Seller.class, id);
      return seller == null ? Optional.empty() : Optional.of(seller);
    }
}
