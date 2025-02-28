package repository.jdbc;

import entities.Product;
import repository.interfaces.ProductRepository;
import util.ColorCodes;
import util.ConnectionUtility;
import util.ResultSetUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductJDBCRepository  implements ProductRepository {
    private List<Product> products;
   private Connection con;
    public ProductJDBCRepository(){
        initSqlDataConn();
    }
    void initSqlDataConn() {
        try {
            this.con = ConnectionUtility.getConnection();
            products = getProducts();
        } catch (SQLException e) {
            System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
        }
    }
    private List<Product> getProducts() throws SQLException {
        PreparedStatement statement = con.prepareStatement("SELECT * FROM PRODUCT");
        return ResultSetUtility.getProductsFromResultSet(statement.executeQuery());
    }
    @Override
    public List<Product> fetchProducts() {
        return products;
    }

    @Override
    public Optional<Product> fetchProductById(Long id) {
        return products.stream().filter(p-> p.getId().equals(id)).findAny();
    }

    @Override
    public Optional<Product> fetchProductByName(String name) {
        return products.stream().filter(p-> p.getName().equalsIgnoreCase(name)).findAny();
    }
}
