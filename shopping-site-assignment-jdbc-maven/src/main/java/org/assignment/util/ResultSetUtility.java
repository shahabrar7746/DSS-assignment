package org.assignment.util;

import org.assignment.entities.Customer;
import org.assignment.entities.Order;
import org.assignment.entities.Product;
import org.assignment.entities.Seller;

import org.assignment.enums.*;
import org.assignment.enums.Currency;

import org.assignment.exceptions.CustomerNotFoundException;
import org.assignment.exceptions.NoProductFoundException;
import org.assignment.repository.interfaces.CustomerRepository;
import org.assignment.repository.interfaces.ProductRepository;
import org.assignment.repository.interfaces.SellerRepository;
import org.assignment.repositoryhibernateimpl.CustomerRepoHibernateImpl;
import org.assignment.repositoryhibernateimpl.ProductRepoHibernateImpl;
import org.assignment.repositoryhibernateimpl.SellerRepoHibernateImpl;
import org.assignment.repositoryjdbcimpl.*;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.*;

public final class ResultSetUtility {
    private static CustomerRepository customerRepository = new CustomerRepoHibernateImpl();
    private static SellerRepository sellerRepository = new SellerRepoHibernateImpl();
    private static ProductRepository productRepository = new ProductRepositoryImpl();


    public static List<Customer> getCustomersFromResultSet(ResultSet set) throws SQLException {

        List<Customer> customers = new ArrayList<>();
        while (set.next()) {
            Long id = set.getLong("customer_id");
            String email = set.getString("email");
            String password = set.getString("password");
            String name = set.getString("name");
            String address = set.getString("address");
            Roles roles = Roles.valueOf(set.getString("role"));
            Timestamp timestamp = set.getTimestamp("registered_on");
            Customer customer = Customer.builder()
                    .registeredOn(timestamp.toLocalDateTime())
                    .id(id)
                    .email(email)
                    .password(password)
                    .address(address)
                    .name(name)
                    .role(roles)
                    .build();
            customers.add(customer);
        }
        set.close();
        return customers;
    }

    public static List<Order> getOrdersFromResultSet(ResultSet set) throws SQLException, CustomerNotFoundException, NoProductFoundException {
        List<Order> orders = new ArrayList<>();
        while (set.next()) {
            Long orderId = set.getLong("order_id");
            Long customerId = set.getLong("customer_id");
            Long productId = set.getLong("product_id");
            OrderStatus orderStatus = OrderStatus.valueOf(set.getString("status"));

            Timestamp orderTimestamp = set.getTimestamp("ordered_on");
            Long sellerId = set.getLong("seller_id");
            Currency currency = Currency.valueOf(set.getString("currency"));
            double price = set.getDouble("price");
            Optional<Customer> customer = customerRepository.fetchById(customerId);
            if (customer.isPresent()) {
                Order order = Order.builder()
                        .orderedOn(orderTimestamp.toLocalDateTime())
                        .seller(sellerRepository.fetchById(sellerId).get())
                        .id(orderId)
                        .product(productRepository.fetchProductById(productId).get())
                        .currency(currency)
                        .price(price)
                        .customer(customerRepository.fetchById(customerId).get())
                        .status(orderStatus)
                        .build();
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
        while (set.next()) {
            Long pid = set.getLong("product_id");
            double price = set.getDouble("price");
            String pName = set.getString("product_name");
            Currency currency = Currency.valueOf(set.getString("currency"));
            ProductType type = ProductType.valueOf(set.getString("product_type"));
//            products.add(new Product(pid,pName,currency,price,type));
        }
        return products;
    }

    public static List<Seller> getSellerFromResultSet(ResultSet set) throws SQLException {
        List<Seller> products = new ArrayList<>();
        while (set.next()) {
            Long sid = set.getLong("seller_id");
            String name = set.getString("name");
            Roles role = Roles.valueOf(set.getString("role"));
            Timestamp timestamp = set.getTimestamp("created_on");
            Seller seller = Seller.builder()
                    .name(name)
                    .id(sid)
                    .role(role)
                    .registeredOn(timestamp.toLocalDateTime())
                    .build();
            products.add(seller);
        }
        return products;
    }

    public static Optional<Order> getOrderByIdFromResultSet(ResultSet set) throws SQLException, CustomerNotFoundException, NoProductFoundException {

        while (set.next()) {
            Long orderId = set.getLong("order_id");
            Long customerId = set.getLong("customer_id");
            Long productId = set.getLong("product_id");
            OrderStatus orderStatus = OrderStatus.valueOf(set.getString("status"));
            Timestamp orderTimestamp = set.getTimestamp("ordered_on");
            Long sellerId = set.getLong("seller_id");
            Currency currency = Currency.valueOf(set.getString("currency"));
            double price = set.getDouble("price");
            Optional<Customer> customer = customerRepository.fetchById(customerId);
            if (customer.isPresent()) {
                Order order = Order.builder()
                        .orderedOn(orderTimestamp.toLocalDateTime())
                        .seller(sellerRepository.fetchById(sellerId).get())
                        .id(orderId)
                        .product(productRepository.fetchProductById(productId).get())
                        .currency(currency)
                        .price(price)
                        .customer(customerRepository.fetchById(customerId).get())
                        .status(orderStatus)
                        .build();
                order.setId(orderId);
                return Optional.of(order);
            } else {
                throw new RuntimeException("Order Id missing: {}");
            }
        }
        return Optional.empty();
    }
}

