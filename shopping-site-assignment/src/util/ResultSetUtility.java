package util;

import entities.Customer;
import entities.Order;
import entities.Product;
import entities.Seller;

import enums.Currency;
import enums.OrderStatus;
import enums.ProductType;
import enums.Roles;

import repository.interfaces.CustomerRepository;
import repository.interfaces.ProductRepository;
import repository.interfaces.SellerRepository;
import repository.jdbc.CustomerJDBCRepository;
import repository.jdbc.ProductJDBCRepository;
import repository.jdbc.SellerJDBCRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class ResultSetUtility {
private static CustomerRepository customerRepository;
private  static SellerRepository sellerRepository;
private static ProductRepository productRepository;

static Map<Long, Customer> map = new ConcurrentHashMap<>();

static { // TODO
        try {
            sellerRepository = new SellerJDBCRepository();
            customerRepository = new CustomerJDBCRepository();
            productRepository = new ProductJDBCRepository();
        } catch (SQLException e) {
            System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
        }
    }

    public static Map<Long, Customer> getCustomersFromResultSet(ResultSet set) throws SQLException {
//        Map<Long, Customer> map = new ConcurrentHashMap<>();

        while(set.next()){
            Long id = set.getLong("customer_id");
            String email = set.getString("email");
            String password = set.getString("password");
            String name = set.getString("name");
            String address = set.getString("address");
            Roles roles  = Roles.valueOf( set.getString("role"));
            Timestamp timestamp = set.getTimestamp("registered_on");
            map.put(id, new Customer(id,name,email, password, address, timestamp, roles));
        }
        set.close();
        return map;
    }
    public static Map<Long, Order> getOrdersFromResultSet(ResultSet set) throws SQLException {
        Map<Long, Order> map = new ConcurrentHashMap<>();
        while(set.next()){
           Long orderId= set.getLong("ORDER_ID");
           Long customerId = set.getLong("customer_id");
           Long productId = set.getLong("product_id");
            OrderStatus orderStatus = OrderStatus.valueOf(set.getString("status"));
            Timestamp orderTimestamp= set.getTimestamp("ordered_on");
            Long sellerId = set.getLong("seller_id");
            Currency currency = Currency.valueOf(set.getString("currency"));
            double price = set.getDouble("price");
            Optional<Customer> customer = customerRepository.fetchById(customerId);
            if(customer.isPresent()){
                map.put(orderId, new Order( orderId, customer.get(), productRepository.fetchProductById(productId).get(), sellerRepository.fetchById(sellerId).get(), orderStatus,currency,orderTimestamp,price ));
            } else {
                throw new RuntimeException("Order Id missing: {}");
            }
        }
        set.close();
        return map;
    }
    public static List<Product> getProductsFromResultSet(ResultSet set) throws SQLException {
        List<Product> products = new ArrayList<>();
        while(set.next()){
            Long pid = set.getLong("product_id");
            double price = set.getDouble("price");
            String pname = set.getString("product_name"); // TODO camelCase
            Currency currency = Currency.valueOf(set.getString("currency"));
            ProductType type = ProductType.valueOf(set.getString("product_type"));
            products.add(new Product(pid,pname,currency,price,type));
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
            products.add(new Seller(sid, name, role, timestamp));
        }
        return products;
    }
}
