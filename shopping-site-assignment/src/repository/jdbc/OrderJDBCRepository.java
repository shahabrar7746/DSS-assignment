package repository.jdbc;

import entities.Order;
import enums.OrderStatus;
import repository.interfaces.OrderRepository;
import util.ColorCodes;
import util.ConnectionUtility;
import util.ResultSetUtility;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class OrderJDBCRepository implements OrderRepository {
    private Connection con;
    private Map<Long, Order> map = new HashMap<>(); // TODO
    public OrderJDBCRepository()  {// TODO
        init();
    }
    private void init(){
        try {
            this.con = ConnectionUtility.getConnection();
            map = helperToFetchOrders();
        } catch (SQLException e) {
            System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
        }
    }
    @Override
    public List<Order> getAllOrders() {
        return map.values().stream().toList();
    }

    @Override
    public Optional<Order> fetchOrderById(Long id) {
        return map.values().stream().filter(o-> o.getOrderId().equals(id)).findAny();
    }

    @Override
    public boolean cancelOrder(Order order) {
      boolean isCancelled = false;
        try {
            isCancelled = helperToRemoveOrder(order);
        } catch (SQLException e) {
            System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
        }
        return isCancelled;
    }

    @Override
    public void addOrder(Order order) {
        try {
            helperToAddOrder(order);
            map = helperToFetchOrders();
        } catch (SQLException e) {
            System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
        }

    }

    @Override
    public List<Order> fetchOrderByProductName(String name) {
        return map.values().stream().filter(o-> o.getProduct().getName().equals(name)).toList();
    }

    @Override
    public List<Order> getOrderByCustomerId(Long cid) {
        return map.values().stream().filter(o -> o.getCustomer().getId().equals(cid)).toList();
    }

    @Override
    public List<Order> getAllDeliveredOrders()  {
        String query = "select orders.* from customer, orders where customer.customer_id = orders.customer_id and orders.status = 'DELIVERED';";
       List<Order> orders = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(query);
        orders = ResultSetUtility.getOrdersFromResultSet(statement.executeQuery()).values().stream().toList();
        } catch (SQLException e) {
            System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
        }
        return orders;
    }

    private Map<Long, Order> helperToFetchOrders() throws SQLException {
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

        statement.setTimestamp(4, Timestamp.valueOf( order.getOrderedOn()));
        statement.setLong(5 , order.getSeller().getId());
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
