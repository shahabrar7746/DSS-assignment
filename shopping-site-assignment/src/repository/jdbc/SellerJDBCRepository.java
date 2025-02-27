package repository.jdbc;

import entities.Product;
import entities.Seller;
import repository.interfaces.SellerRepository;
import util.ColorCodes;
import util.ConnectionUtility;
import util.ResultSetUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;
import java.util.Optional;

public class SellerJDBCRepository implements SellerRepository {
  private   List<Seller> sellers;
    private Connection con;
    public SellerJDBCRepository(){

        try {
            con = ConnectionUtility.getConnection();
            sellers = getAllSellers();
            System.out.println(sellers);
        } catch (SQLException e) {
            System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
        }
    }
    @Override
    public List<Seller> fetchSellers() {
        return sellers;
    }

    @Override
    public Optional<Seller> fetchById(Long id) {
        return sellers.stream().filter(s-> s.getId().equals(id)).findAny();
    }
    private List<Seller> getAllSellers() throws SQLException {
        String query = "SELECT * FROM SELLER";

        PreparedStatement statement = con.prepareStatement(query);
        return ResultSetUtility.getSellerFromResultSet(statement.executeQuery());
    }
}
