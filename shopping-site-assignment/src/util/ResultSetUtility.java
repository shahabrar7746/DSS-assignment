package util;

import entities.Customer;
import entities.Order;
import entities.Product;
import entities.Seller;

import enums.*;

import enums.Currency;
import exceptions.CustomerNotFoundException;
import repository.interfaces.CustomerRepository;
import repository.interfaces.ProductRepository;
import repository.interfaces.SellerRepository;
import repository.jdbc.CustomerJDBCRepository;
import repository.jdbc.ProductJDBCRepository;
import repository.jdbc.SellerJDBCRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.util.*;

import java.util.concurrent.ConcurrentHashMap;

public final class ResultSetUtility {
private static CustomerRepository  customerRepository = new CustomerJDBCRepository();
private  static SellerRepository   sellerRepository = new SellerJDBCRepository();
private static ProductRepository productRepository = new ProductJDBCRepository();




    public static Map<Long, Customer> getCustomersFromResultSet(ResultSet set) throws SQLException {
        Map<Long, Customer> map = new ConcurrentHashMap<>();

        while(set.next()){
            Long id = set.getLong("customer_id");
            String email = set.getString("email");
            String password = set.getString("password");
            String name = set.getString("name");
            String address = set.getString("address");
            Roles roles  = Roles.valueOf( set.getString("role"));
            Timestamp timestamp = set.getTimestamp("registered_on");
            Customer customer = new Customer(name,email, password, address, timestamp.toLocalDateTime(), roles);
            customer.setId(id);
            map.put(id, customer);
        }
        set.close();
        return map;
    }
    public static List<Order> getOrdersFromResultSet(ResultSet set) throws SQLException, CustomerNotFoundException {
        List<Order> orders = new ArrayList<>();
        while(set.next()){
           Long orderId= set.getLong("order_id");
           Long customerId = set.getLong("customer_id");
           Long productId = set.getLong("product_id");
            OrderStatus orderStatus = OrderStatus.valueOf(set.getString("status"));

            Timestamp orderTimestamp= set.getTimestamp("ordered_on");
            Long sellerId = set.getLong("seller_id");
            Currency currency = Currency.valueOf(set.getString("currency"));
            double price = set.getDouble("price");
            Optional<Customer> customer = customerRepository.fetchById(customerId);
            if(customer.isPresent()){
                Order order =  new Order(customer.get(), productRepository.fetchProductById(productId).get(), sellerRepository.fetchById(sellerId).get(), orderStatus,currency,orderTimestamp.toLocalDateTime(),price);
                order.setId(orderId);
                orders.add(order);
            } else {
                throw new RuntimeException("Order Id missing: {}");
            }
        }
        set.close();
        return orders;
    }
    public static List<Product> getProductsFromResultSet(ResultSet set) throws SQLException {
        List<Product> products = new ArrayList<>();
        while(set.next()){
            Long pid = set.getLong("product_id");
            double price = set.getDouble("price");
            String pName = set.getString("product_name"); // TODO camelCase
            Currency currency = Currency.valueOf(set.getString("currency"));
            ProductType type = ProductType.valueOf(set.getString("product_type"));
            products.add(new Product(pid,pName,currency,price,type));
        }
        return  products;
    }
    public static List<Seller> getSellerFromResultSet(ResultSet set) throws SQLException {
        List<Seller> products = new ArrayList<>();
        while (set.next()){
            Long sid = set.getLong("seller_id");
            String name = set.getString("name");
            Roles role = Roles.valueOf(set.getString("role"));
            Timestamp  timestamp = set.getTimestamp("created_on");
            products.add(new Seller(sid, name, role, timestamp.toLocalDateTime()));
        }
        return products;
    }
    public static  Optional<Order> getOrderByIdFromResultSet(ResultSet set) throws SQLException, CustomerNotFoundException {

        while (set.next()){
        Long orderId= set.getLong("order_id");
        Long customerId = set.getLong("customer_id");
        Long productId = set.getLong("product_id");
        OrderStatus orderStatus = OrderStatus.valueOf(set.getString("status"));

        Timestamp orderTimestamp= set.getTimestamp("ordered_on");
        Long sellerId = set.getLong("seller_id");
        Currency currency = Currency.valueOf(set.getString("currency"));
        double price = set.getDouble("price");
        Optional<Customer> customer = customerRepository.fetchById(customerId);
        if(customer.isPresent()){
            Order order =  new Order(customer.get(), productRepository.fetchProductById(productId).get(), sellerRepository.fetchById(sellerId).get(), orderStatus,currency,orderTimestamp.toLocalDateTime(),price);
            order.setId(orderId);
            return Optional.of(order);
        } else {
            throw new RuntimeException("Order Id missing: {}");
        }
    }
        return Optional.empty();
    }
}

