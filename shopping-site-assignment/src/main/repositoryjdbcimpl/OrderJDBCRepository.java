package main.repositoryjdbcimpl;

import main.entities.Order;
import main.enums.OrderStatus;
import main.exceptions.CustomerNotFoundException;
import main.exceptions.NoProductFoundException;
import main.exceptions.OrderNotFoundException;
import main.repository.interfaces.OrderRepository;
import main.util.ColorCodes;
import main.util.ConnectionUtility;
import main.util.ResultSetUtility;

import java.sql.*;
import java.util.*;

public class OrderJDBCRepository implements OrderRepository {
    private Connection con;


    public OrderJDBCRepository() {// TODO
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
        return helperToFetchOrders();
    }

    @Override
    public Optional<Order> fetchOrderById(Long id) throws Exception {
        String query = "SELECT * FROM ORDERS where order_id = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, id);
        return ResultSetUtility.getOrderByIdFromResultSet(statement.executeQuery());
    }

    @Override
    public boolean cancelOrder(Order order) {
        boolean isCancelled = false;
        try {
            isCancelled = helperToRemoveOrder(order);
        } catch (SQLException e) {
            return  false;
        }
        return isCancelled;
    }

    @Override
    public void addOrder(Order order) throws SQLException {
        helperToAddOrder(order);
    }

    @Override
    public List<Order> fetchOrderByProductName(String name) throws SQLException, CustomerNotFoundException, NoProductFoundException {
        String query = "SELECT ORDER.* FROM ORDERS, PRODUCT WHERE orders.product_id = product.product_id AND product.product_name = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, name.toUpperCase());
        return ResultSetUtility.getOrdersFromResultSet(statement.executeQuery());
    }

    @Override
    public List<Order> getOrderByCustomerId(Long cid) throws SQLException, CustomerNotFoundException, NoProductFoundException {
        String query = "SELECT * FROM ORDERS WHERE customer_id = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, cid);
        return ResultSetUtility.getOrdersFromResultSet(statement.executeQuery());
    }

    @Override
    public List<Order> getAllDeliveredOrders() throws SQLException, CustomerNotFoundException, OrderNotFoundException, NoProductFoundException {
        String query = "select orders.* from customer, orders where customer.customer_id = orders.customer_id and orders.status = 'DELIVERED';";
        List<Order> orders = new ArrayList<>();
        PreparedStatement statement = null;
        statement = con.prepareStatement(query);
        orders = ResultSetUtility.getOrdersFromResultSet(statement.executeQuery());
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("No delievered orders found");
        }
        return orders;
    }

    private List<Order> helperToFetchOrders() throws Exception {
        String query = "select * from orders";
        PreparedStatement statement = con.prepareStatement(query);
        return ResultSetUtility.getOrdersFromResultSet(statement.executeQuery());
    }

    private void helperToAddOrder(Order order) throws SQLException {
        String query = "INSERT INTO ORDERS(CUSTOMER_ID, PRODUCT_ID, STATUS, ORDERED_ON, SELLER_ID, CURRENCY, PRICE) " +
                "VALUES(?,?,?, ?, ?, ?, ?)";
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

    private boolean helperToRemoveOrder(Order order) throws SQLException {
        String query = "DELETE FROM ORDERS WHERE ORDER_ID = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, order.getOrderId());
        return statement.executeUpdate() == 1;
    }
}
