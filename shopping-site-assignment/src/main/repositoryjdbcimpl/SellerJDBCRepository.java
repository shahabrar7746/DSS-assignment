package main.repositoryjdbcimpl;

import main.entities.Seller;
import main.repository.interfaces.SellerRepository;
import main.util.ConnectionUtility;
import main.util.ResultSetUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;
import java.util.Optional;

public class SellerJDBCRepository implements SellerRepository {

    private Connection con;

    public SellerJDBCRepository() { // TODO update constructor remove unnecessary initialization create initMethod for initilizing instance
        init();
    }

    private void init() {
        con = ConnectionUtility.getConnection();
    }

    @Override
    public List<Seller> fetchSellers() throws SQLException {
        return getAllSellers();
    }

    @Override
    public Optional<Seller> fetchById(Long id) throws SQLException {
        return
                fetchSellers().stream().filter(s -> s.getId().equals(id)).findAny();
    }

    private List<Seller> getAllSellers() throws SQLException { // TODO handle errors within method
        String query = "SELECT * FROM SELLER"; // TODO create queriesUtils
        PreparedStatement statement = con.prepareStatement(query);
        return ResultSetUtility.getSellerFromResultSet(statement.executeQuery());
    }

}
