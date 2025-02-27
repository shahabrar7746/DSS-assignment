package repository.interfaces;

import entities.Seller;

import java.util.List;
import java.util.Optional;

public interface SellerRepository {
List<Seller> fetchSellers();
Optional<Seller> fetchById(Long id);
}
