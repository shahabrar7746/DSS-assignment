package org.assignment.repositoryjdbcimpl;

import org.assignment.entities.Seller;
import org.assignment.queries.SellerQueries;
import org.assignment.repository.interfaces.SellerRepository;
import org.assignment.util.ConnectionUtility;
import org.assignment.util.ResultSetUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;
import java.util.Optional;

public class SellerRepositoryImpl implements SellerRepository {

    private Connection con;

    public SellerRepositoryImpl() { // TODO update constructor remove unnecessary initialization create initMethod for initilizing instance
        init();
    }

    private void init() {
        con = ConnectionUtility.getConnection();
    }

    @Override
    public List<Seller> fetchSellers() throws SQLException {
        String query = SellerQueries.getAllSellersQuery();
        PreparedStatement statement = con.prepareStatement(query);
        return ResultSetUtility.getSellerFromResultSet(statement.executeQuery());
    }

    @Override
    public Optional<Seller> fetchById(Long id) throws SQLException {
    String query = SellerQueries.getSellersByColumnQuery(new String[]{"SELLER_ID"}, null);
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, id);
        List<Seller> sellers =  ResultSetUtility.getSellerFromResultSet(statement.executeQuery());
       return sellers.isEmpty() ? Optional.empty() : Optional.of(sellers.get(0));
    }

}
