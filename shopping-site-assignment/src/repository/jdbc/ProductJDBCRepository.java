package repository.jdbc;

import entities.Product;
import entities.Seller;
import exceptions.NoProductFoundException;
import repository.interfaces.ProductRepository;
import util.ColorCodes;
import util.ConnectionUtility;
import util.ResultSetUtility;

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
    public List<Product> fetchProducts() throws NoProductFoundException {
        List<Product> products = helperToFetchProducts();
        if(products.isEmpty()) {
            throw new NoProductFoundException("Product repository is empty");
        }
        return products;
    }

    @Override
    public Optional<Product> fetchProductById(Long id) {
        Optional<Product> optionalProduct = products.stream().filter(p -> p.getId().equals(id)).findAny();
         if (optionalProduct.isEmpty()){
             //throw no orders found.
         }
         return optionalProduct;
    }

    @Override
    public Optional<Product> fetchProductByName(String name) {
        return products.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findAny();
    }
    private List<Seller> helperToFetchProducts(){
        String query = "SELECT * FROM PRODUCT";
    }
}
