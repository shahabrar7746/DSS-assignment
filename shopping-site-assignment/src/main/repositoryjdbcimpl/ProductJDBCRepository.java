package main.repositoryjdbcimpl;

import main.entities.Product;
import main.exceptions.NoProductFoundException;
import main.repository.interfaces.ProductRepository;
import main.util.ConnectionUtility;
import main.util.ResultSetUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductJDBCRepository implements ProductRepository {

    private Connection con;

    public ProductJDBCRepository() {
        initSqlDataConn();
    }

    void initSqlDataConn() {
            this.con = ConnectionUtility.getConnection();
    }

    private List<Product> getProducts() throws SQLException {
        PreparedStatement statement = con.prepareStatement("SELECT * FROM PRODUCT");
        return ResultSetUtility.getProductsFromResultSet(statement.executeQuery());
    }

    @Override
    public List<Product> fetchProducts() throws NoProductFoundException, SQLException {
        List<Product> products = getProducts();
        if(products.isEmpty()) {
            throw new NoProductFoundException("Product main.repository is empty");
        }
        return products;
    }

    @Override
    public Optional<Product> fetchProductById(Long id) throws SQLException, NoProductFoundException {
        PreparedStatement statement = con.prepareStatement("SELECT * FROM PRODUCT WHERE PRODUCT_ID = ?");
        statement.setLong(1, id);
        List<Product> products =  ResultSetUtility.getProductsFromResultSet(statement.executeQuery());
         if (products.isEmpty()){
             throw new NoProductFoundException("Product not found");
         }
         return Optional.of(products.get(0));
    }

    @Override
    public Optional<Product> fetchProductByName(String name) throws SQLException, NoProductFoundException {
        PreparedStatement statement = con.prepareStatement("SELECT * FROM PRODUCT WHERE PRODUCT_NAME = ?");
        statement.setString(1, name);
        List<Product> products =  ResultSetUtility.getProductsFromResultSet(statement.executeQuery());
        if (products.isEmpty()){
            throw new NoProductFoundException("Product not found");
        }
        return Optional.of(products.get(0));
    }

}
