//package org.assignment.util;
//
//import org.assignment.entities.User;
//import org.assignment.entities.Order;
//import org.assignment.entities.Product;
//
//import org.assignment.enums.*;
//import org.assignment.enums.Currency;
//
//import org.assignment.exceptions.CustomerNotFoundException;
//import org.assignment.exceptions.NoProductFoundException;
//import org.assignment.repository.interfaces.UserRepository;
//import org.assignment.repository.interfaces.ProductRepository;
//import org.assignment.repositoryhibernateimpl.UserRepoHibernateImpl;
//import org.assignment.repositoryjdbcimpl.*;
//
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//
//import java.util.*;
//public final class ResultSetUtility {
//    private static UserRepository repository = new UserRepoHibernateImpl(ConnectionUtility.getEntityManager(), ConnectionUtility.getEntityManager().getTransaction());
//    private static ProductRepository productRepository = new ProductRepositoryImpl();
//
//
//    public static List<User> getCustomersFromResultSet(ResultSet set) throws SQLException {
//
//        List<User> users = new ArrayList<>();
//        while (set.next()) {
//            Long id = set.getLong("customer_id");
//            String email = set.getString("email");
//            String password = set.getString("password");
//            String name = set.getString("name");
//            String address = set.getString("address");
//            Roles roles = Roles.valueOf(set.getString("role"));
//            Timestamp timestamp = set.getTimestamp("registered_on");
//            User user = User.builder()
//                    .registeredOn(timestamp.toLocalDateTime())
//                    .id(id)
//                    .email(email)
//                    .password(password)
//                    .address(address)
//                    .name(name)
//                    .role(roles)
//                    .build();
//            users.add(user);
//        }
//        set.close();
//        return users;
//    }
//
//    public static List<Order> getOrdersFromResultSet(ResultSet set) throws SQLException, CustomerNotFoundException, NoProductFoundException {
//        List<Order> orders = new ArrayList<>();
//        while (set.next()) {
//            Long orderId = set.getLong("order_id");
//            Long customerId = set.getLong("customer_id");
//            Long productId = set.getLong("product_id");
//            OrderStatus orderStatus = OrderStatus.valueOf(set.getString("status"));
//
//            Timestamp orderTimestamp = set.getTimestamp("ordered_on");
//            Long sellerId = set.getLong("seller_id");
//            Currency currency = Currency.valueOf(set.getString("currency"));
//            double price = set.getDouble("price");
//            Optional<User> customer = repository.fetchById(customerId);
//            if (customer.isPresent()) {
//                Order order = Order.builder()
//                        .orderedOn(orderTimestamp.toLocalDateTime())
//                        .id(orderId)
//                        .product(productRepository.fetchProductById(productId).get())
//                        .price(price)
//                        .user(repository.fetchById(customerId).get())
//                        .status(orderStatus)
//                        .build();
//                orders.add(order);
//            } else {
//                throw new RuntimeException("Order Id missing: {}");
//            }
//        }
//        set.close();
//        return orders;
//    }
//
//    public static List<Product> getProductsFromResultSet(ResultSet set) throws SQLException {
//        List<Product> products = new ArrayList<>();
//        while (set.next()) {
//            Long pid = set.getLong("product_id");
//            double price = set.getDouble("price");
//            String pName = set.getString("product_name");
//            Currency currency = Currency.valueOf(set.getString("currency"));
//            ProductType type = ProductType.valueOf(set.getString("product_type"));
////            products.add(new Product(pid,pName,currency,price,type));
//        }
//        return products;
//    }
//
//    public static List<User> getSellerFromResultSet(ResultSet set) throws SQLException {
//        List<User> products = new ArrayList<>();
//        while (set.next()) {
//            Long sid = set.getLong("seller_id");
//            String name = set.getString("name");
//            Roles role = Roles.valueOf(set.getString("role"));
//            Timestamp timestamp = set.getTimestamp("created_on");
//            products.add(new User());
//        }
//        return products;
//    }
//
//    public static Optional<Order> getOrderByIdFromResultSet(ResultSet set) throws SQLException, CustomerNotFoundException, NoProductFoundException {
//
//        while (set.next()) {
//            Long orderId = set.getLong("order_id");
//            Long customerId = set.getLong("customer_id");
//            Long productId = set.getLong("product_id");
//            OrderStatus orderStatus = OrderStatus.valueOf(set.getString("status"));
//            Timestamp orderTimestamp = set.getTimestamp("ordered_on");
//            Long sellerId = set.getLong("seller_id");
//            Currency currency = Currency.valueOf(set.getString("currency"));
//            double price = set.getDouble("price");
//            Optional<User> customer = repository.fetchById(customerId);
//            if (customer.isPresent()) {
//                Order order = Order.builder()
//                        .orderedOn(orderTimestamp.toLocalDateTime())
//                        .id(orderId)
//                        .product(productRepository.fetchProductById(productId).get())
//                        .price(price)
//                        .user(repository.fetchById(customerId).get())
//                        .status(orderStatus)
//                        .build();
//                order.setId(orderId);
//                return Optional.of(order);
//            } else {
//                throw new RuntimeException("Order Id missing: {}");
//            }
//        }
//        return Optional.empty();
//    }
//}
//
