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
  private  List<Seller> sellers; // TODO initialize
    private Connection con;
    public SellerJDBCRepository(){ // TODO update constructor remove unnecessary initialization create initMethod for initilizing instance

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
    private List<Seller> getAllSellers() throws SQLException { // TODO handle errors within method
        String query = "SELECT * FROM SELLER"; // TODO create queriesUtils

        PreparedStatement statement = con.prepareStatement(query);
        return ResultSetUtility.getSellerFromResultSet(statement.executeQuery());
    }
}
