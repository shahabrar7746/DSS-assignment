package repository.interfaces;

import entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> fetchProducts();
    Optional<Product> fetchProductById(Long id);
    Optional<Product> fetchProductByName(String name);
}
