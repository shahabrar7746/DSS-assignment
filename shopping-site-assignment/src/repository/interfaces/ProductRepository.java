package repository.interfaces;

import entities.Product;
import exceptions.NoProductFoundException;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> fetchProducts() throws NoProductFoundException;
    Optional<Product> fetchProductById(Long id);
    Optional<Product> fetchProductByName(String name);
}
