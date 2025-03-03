package main.repository.interfaces;

import main.entities.Product;
import main.exceptions.NoProductFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> fetchProducts() throws NoProductFoundException, SQLException;
    Optional<Product> fetchProductById(Long id) throws SQLException, NoProductFoundException;
    Optional<Product> fetchProductByName(String name) throws SQLException, NoProductFoundException;
}
