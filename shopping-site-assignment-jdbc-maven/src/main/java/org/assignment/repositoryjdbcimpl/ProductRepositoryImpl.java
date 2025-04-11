package org.assignment.repositoryjdbcimpl;

import org.assignment.entities.Product;
import org.assignment.exceptions.NoProductFoundException;
import org.assignment.queries.ProductQueries;
import org.assignment.repository.interfaces.ProductRepository;
import org.assignment.util.ConnectionUtility;
import org.assignment.util.ResultSetUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository {

    private Connection con;

    public ProductRepositoryImpl() {
        initSqlDataConn();
    }

    void initSqlDataConn() {
            this.con = ConnectionUtility.getConnection();
    }

    @Override
    public List<Product> fetchProducts() throws SQLException {
        String query = ProductQueries.getAllProducts();
        PreparedStatement statement = con.prepareStatement(query);
        List<Product> products = ResultSetUtility.getProductsFromResultSet(statement.executeQuery());
        return products;
    }

    @Override
    public Product updateProduct(Product product) {
        return null;
    }

    @Override
    public Optional<Product> fetchProductById(Long id) throws SQLException {
        Optional<Product> productOptional = Optional.empty();
        String query = ProductQueries.getProductsByColumns(new String[]{"PRODUCT_ID"}, null);
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, id);
        List<Product> products =  ResultSetUtility.getProductsFromResultSet(statement.executeQuery());
         return products.isEmpty() ? productOptional : Optional.of(products.get(0));
    }

    @Override
    public Optional<Product> fetchProductByName(String name) throws SQLException {
        Optional<Product> productOptional = Optional.empty();
        String query = ProductQueries.getProductsByColumns(new String[]{"PRODUCT_NAME"}, null);
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, name);
        List<Product> products =  ResultSetUtility.getProductsFromResultSet(statement.executeQuery());
        return products.isEmpty() ? productOptional : Optional.of(products.get(0));
    }

    @Override
    public void addProduct(Product product) {

    }
}
