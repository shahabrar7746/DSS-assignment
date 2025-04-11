package org.assignment.repositoryjdbcimpl;

import org.assignment.entities.User;
import org.assignment.entities.Order;
import org.assignment.entities.Product;
import org.assignment.enums.OrderStatus;
import org.assignment.exceptions.CustomerNotFoundException;
import org.assignment.exceptions.NoProductFoundException;
import org.assignment.exceptions.OrderNotFoundException;
import org.assignment.queries.OrderQueries;
import org.assignment.repository.interfaces.OrderRepository;
import org.assignment.util.ColorCodes;
import org.assignment.util.ConnectionUtility;
import org.assignment.util.ResultSetUtility;

import java.sql.*;
import java.util.*;

public class OrderRepositoryImpl implements OrderRepository {
    private Connection con;

    public OrderRepositoryImpl() {
        init();
    }

    @Override
    public List<Order> getOrdersByStatusAndCustomer(User user, OrderStatus status) {
        return List.of();
    }

    private void init() {
        try {
            this.con = ConnectionUtility.getConnection();

        } catch (Exception e) {
            System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
        }
    }

    @Override
    public List<Order> getAllOrders() throws SQLException, CustomerNotFoundException, NoProductFoundException {
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
    public void cancelOrder(Order order) throws SQLException {
        String query = OrderQueries.deleteOrderQuery("ORDER_ID");
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, order.getId());
    }

    @Override
    public Order addOrder(Order order) throws SQLException {
        String columns[] = {"CUSTOMER_ID", "PRODUCT_ID", "STATUS", "ORDERED_ON", "SELLER_ID", "CURRENCY", "PRICE"};
        String query = OrderQueries.addOrderQuery(columns);
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, order.getUser().getId());
        statement.setLong(2, order.getProduct().getId());
        statement.setObject(3, OrderStatus.ORDERED, Types.OTHER);
        statement.setTimestamp(4, Timestamp.valueOf(order.getOrderedOn()));
        statement.setDouble(7, order.getPrice());
        statement.executeUpdate();
        return order;
    }

    @Override
    public List<Order> fetchOrderByProductAndCustomer(Product product, User user) throws SQLException, CustomerNotFoundException, NoProductFoundException {
        String query = OrderQueries.getOrdersByProductAndCustomer();
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, user.getId());
        statement.setLong(2, product.getId());
        return ResultSetUtility.getOrdersFromResultSet(statement.executeQuery());
    }

    @Override
    public List<Order> getOrderByCustomer(User user) throws SQLException, CustomerNotFoundException, NoProductFoundException {
        String query = OrderQueries.getOrdersByColumns(new String[]{"CUSTOMER_ID"}, null);
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, user.getId());
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

    @Override
    public Order updateOrder(Order order) {
        return null;
    }
}
