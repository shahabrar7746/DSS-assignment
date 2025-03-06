package main.repositoryjdbcimpl;

import main.entities.Order;
import main.enums.OrderStatus;
import main.exceptions.CustomerNotFoundException;
import main.exceptions.NoProductFoundException;
import main.exceptions.OrderNotFoundException;
import main.queries.OrderQueries;
import main.repository.interfaces.OrderRepository;
import main.util.ColorCodes;
import main.util.ConnectionUtility;
import main.util.ResultSetUtility;

import javax.management.Query;
import java.sql.*;
import java.util.*;

public class OrderRepositoryImpl implements OrderRepository {
    private Connection con;


    public OrderRepositoryImpl() {// TODO
        init();
    }

    private void init() {
        try {
            this.con = ConnectionUtility.getConnection();

        } catch (Exception e) {
            System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
        }
    }

    @Override
    public List<Order> getAllOrders() throws Exception {
        String query = OrderQueries.getAllOrdersQuery();
        PreparedStatement statement = con.prepareStatement(query);
        return ResultSetUtility.getOrdersFromResultSet(statement.executeQuery());
    }

    @Override
    public Optional<Order> fetchOrderById(Long id) throws Exception {
        String query = OrderQueries.getOrdersByColumns(new String[]{"ORDER_ID"}, null);
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, id);
        return ResultSetUtility.getOrderByIdFromResultSet(statement.executeQuery());
    }

    @Override
    public boolean cancelOrder(Order order) throws SQLException {
        String query = OrderQueries.deleteOrderQuery("ORDER_ID");
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, order.getOrderId());
        return statement.executeUpdate() == 1;
    }

    @Override
    public void addOrder(Order order) throws SQLException {
        String columns[] = {"CUSTOMER_ID", "PRODUCT_ID", "STATUS", "ORDERED_ON", "SELLER_ID", "CURRENCY", "PRICE"};
        String query = OrderQueries.addOrderQuery(columns);
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, order.getCustomer().getId());
        statement.setLong(2, order.getProduct().getId());
        statement.setObject(3, OrderStatus.ORDERED, Types.OTHER);
        statement.setTimestamp(4, Timestamp.valueOf(order.getOrderedOn()));
        statement.setLong(5, order.getSeller().getId());
        statement.setObject(6, order.getCurrency(), Types.OTHER);
        statement.setDouble(7, order.getPrice());
        statement.executeUpdate();
    }

    @Override
    public List<Order> fetchOrderByProductName(String name) throws SQLException, CustomerNotFoundException, NoProductFoundException {
        String query = OrderQueries.getOrdersByProductName();
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, name.toUpperCase());
        return ResultSetUtility.getOrdersFromResultSet(statement.executeQuery());
    }

    @Override
    public List<Order> getOrderByCustomerId(Long cid) throws SQLException, CustomerNotFoundException, NoProductFoundException {
        String query = OrderQueries.getOrdersByColumns(new String[]{"CUSTOMER_ID"}, null);
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, cid);
        return ResultSetUtility.getOrdersFromResultSet(statement.executeQuery());
    }

    @Override
    public List<Order> getAllDeliveredOrders() throws SQLException, CustomerNotFoundException, OrderNotFoundException, NoProductFoundException {
        String query = OrderQueries.deliveredOrderQuery();
        List<Order> orders = new ArrayList<>();
        PreparedStatement statement = null;
        statement = con.prepareStatement(query);
        orders = ResultSetUtility.getOrdersFromResultSet(statement.executeQuery());
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("No delievered orders found");
        }
        return orders;
    }
}
