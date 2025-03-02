package repository.interfaces;

import entities.Seller;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface SellerRepository {
List<Seller> fetchSellers() throws SQLException;
Optional<Seller> fetchById(Long id) throws SQLException;
}
