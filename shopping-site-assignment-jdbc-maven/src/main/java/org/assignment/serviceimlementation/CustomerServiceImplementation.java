package org.assignment.serviceimlementation;

import jakarta.persistence.PersistenceException;

import org.assignment.entities.*;

import org.assignment.enums.Currency;
import org.assignment.enums.OrderStatus;
import org.assignment.enums.ResponseStatus;

import org.assignment.enums.Roles;
import org.assignment.exceptions.CustomerNotFoundException;
import org.assignment.exceptions.EmptyCartException;
import org.assignment.exceptions.NoProductFoundException;
import org.assignment.exceptions.OrderNotFoundException;

import org.assignment.repository.interfaces.CustomerRepository;
import org.assignment.repository.interfaces.OrderRepository;
import org.assignment.repository.interfaces.ProductRepository;
import org.assignment.repository.interfaces.SellerRepository;
import org.assignment.repositoryhibernateimpl.CustomerRepoHibernateImpl;
import org.assignment.repositoryhibernateimpl.OrderRepoHibernateImpl;
import org.assignment.repositoryhibernateimpl.ProductRepoHibernateImpl;
import org.assignment.repositoryhibernateimpl.SellerRepoHibernateImpl;

import org.assignment.services.CustomerService;

import org.assignment.util.ColorCodes;
import org.assignment.util.LogUtil;
import org.assignment.util.Response;
import org.assignment.wrappers.ProductWrappers;
import org.hibernate.HibernateException;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerServiceImplementation implements CustomerService {
    private Scanner sc;
    private List<Product> cart;
    private CustomerRepository repository;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private SellerRepository sellerRepository;
    private static CustomerServiceImplementation service;
    private CustomerRepository customerRepository;

    public static CustomerServiceImplementation getInstance() {
        if (service == null) {
            service = new CustomerServiceImplementation();
        }
        return service;
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        Optional<Customer> optionalCustomer = null;
        try {
            optionalCustomer = repository.fetchByEmail(email);
        } catch (SQLException | PersistenceException e) {
            LogUtil.logError(e.getStackTrace());
        }
        return optionalCustomer == null ? Optional.empty() : optionalCustomer;
    }


    private CustomerServiceImplementation() {
        init();
    }

    private void init() {
        this.sellerRepository = SellerRepoHibernateImpl.getInstance();
        this.productRepository = ProductRepoHibernateImpl.getInstance();
        this.cart = new ArrayList<>();
        this.orderRepository = OrderRepoHibernateImpl.getInstance();
        this.sc = new Scanner(System.in);
        this.repository = CustomerRepoHibernateImpl.getInstance();
        this.customerRepository = CustomerRepoHibernateImpl.getInstance();
    }

    public Response getAllOrders(Customer customer) {
        List<Order> orders = null;
        try {
            orders = orderRepository.getOrderByCustomer(customer);
        } catch (CustomerNotFoundException | NoProductFoundException | OrderNotFoundException e) {
            return new Response(null, e.getLocalizedMessage());
        } catch (SQLException | HibernateException e) {
            return LogUtil.logError(e.getStackTrace());
        }
        return orders.isEmpty() ? new Response(null, "No order found") : new Response(orders);
    }

    private Response bookOrder(Customer customer, Product product) {
        Optional<Seller> optionalSeller = null;
        try {
            optionalSeller = sellerRepository.fetchById(1L);
        } catch (SQLException e) {
            return LogUtil.logError(e.getStackTrace());
        }
        if (optionalSeller.isEmpty()) {
            return new Response(null, "No seller found for this product");
        }

        Order order = new Order(customer, product, optionalSeller.get(), OrderStatus.ORDERED, Currency.INR, LocalDateTime.now(), product.getPrice());
        try {
            orderRepository.addOrder(order);
        } catch (SQLException | HibernateException e) {
            return LogUtil.logError(e.getStackTrace());
        }
        return new Response("");
    }

    /**
     * @param customer whose order need to be cancelled.
     * @throws OrderNotFoundException if no orders found for the customer or order list is empty of the provided customer.
     * @see #bookOrder(Customer, Product)
     */

    public Response cancelOrder(int count, Customer customer, String productName) {
        Optional<List<Order>> order = Optional.empty();
        Response resp = null;
        List<Order> orders = null;
        try {
            Optional<Product> product = productRepository.fetchProductByName(productName);
            if (product.isEmpty()) {
                resp = new Response(null, "Incorrect product name");
            } else {
                orders = orderRepository.fetchOrderByProductAndCustomer(product.get(), customer);
            }

        } catch (CustomerNotFoundException | NoProductFoundException e) {
            resp = new Response(null, e.getLocalizedMessage());
        } catch (SQLException | HibernateException e) {
            resp = LogUtil.logError(e.getStackTrace());
        }
        if (orders != null && !orders.isEmpty()) {
            order = Optional.of(orders);
        }
        if (resp == null && orders.size() > 1 && count == 0) {
            resp = new Response(ResponseStatus.PROCESSING, new ProductWrappers(productName, orders.size()), null);
        }
        if (resp == null && order.isPresent()) {
            try {
                helperForCancelOrder(count, order.get(), customer);
            } catch (SQLException e) {
                resp = new Response(ResponseStatus.ERROR, null, "Some error occured");
            }
        }
        if (resp == null && order.isPresent()) {
            resp = new Response("Order cancelled..");
        }
        return resp;
    }


    private void helperForCancelOrder(int count, List<Order> l, Customer customer) throws SQLException {
        int index = 0;
        while (index != count) {
            Order removedOrder = l.get(index);
            orderRepository.cancelOrder(removedOrder);
            index++;
        }
    }


    public Response intiateCart(Customer customer, String name) {
        Response response = null;
        Optional<Product> product = null;
        Seller s = null;
        try {
            product = productRepository.fetchProductByName(name);
            s = sellerRepository.fetchById(1L).get();
            if (product.isPresent()) {
                final Product p = product.get();
                CartItems item = CartItems.builder()
                        .customer(customer)
                        .seller(s)
                        .product(p)
                        .build();
                customer.getCart().add(item);
                customerRepository.updateCustomer(customer);
                response = new Response("Item added");
            } else {
                response = new Response(null, "Invalid product name please try again.");
            }
        } catch (NoProductFoundException e) {
            response = new Response(null, e.getLocalizedMessage());
        } catch (SQLException e) {
            response = LogUtil.logError(e.getStackTrace());
        }

        return response;
    }


    public Response removeFromCart(Customer customer, String pname) {
        Response response = null;
        List<CartItems> cartItems = customer.getCart();
        cartItems = cartItems
                .stream()
                .filter(c -> !c.getProduct()
                        .getName()
                        .equalsIgnoreCase(pname))
                .toList();
        if (cartItems.size() == customer.getCart().size()) {
            response = new Response(ResponseStatus.ERROR, "", "Could not find product");
        } else {
            customer.setCart(new ArrayList<>(cartItems));
            try {
                customerRepository.updateCustomer(customer);
                response = new Response(ResponseStatus.SUCCESSFUL, "Removed successfully", null);
            } catch (SQLException | PersistenceException e) {
                response = LogUtil.logError(e.getStackTrace());
            }
        }
        return response;
    }

    public Response hasProduct(Customer customer, String productName) {
        Response resp = null;
        boolean exists = customer
                .getCart()
                .stream()
                .filter(item -> item.getProduct()
                        .getName()
                        .equalsIgnoreCase(productName))
                .findFirst()
                .isPresent();
        return new Response(ResponseStatus.SUCCESSFUL, exists, null);
    }

    public Response orderFromCart(Customer customer, String name) {
        Response resp = null;
        CartItems item = customer
                .getCart()
                .stream()
                .filter(i -> i.getProduct().getName().equalsIgnoreCase(name))
                .findFirst()
                .get();
        Order order = Order.builder()
                .orderedOn(LocalDateTime.now())
                .customer(customer)
                .price(item.getProduct().getPrice())
                .seller(item.getSeller())
                .status(OrderStatus.ORDERED)
                .currency(Currency.INR)
                .product(item.getProduct())
                .build();
        try {
            orderRepository.addOrder(order);
            List<CartItems> newCart = customer
                    .getCart()
                    .stream()
                    .filter(i -> !i.getProduct().getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());
            customer.setCart(newCart);
            newCart.forEach(cartItems -> cartItems.setCustomer(customer));
            customerRepository.updateCustomer(customer);

            resp = new Response(ResponseStatus.SUCCESSFUL, "Ordered", null);
        } catch (SQLException e) {
            resp = new Response(ResponseStatus.ERROR, null, "some error occured");
        } catch (Exception e) {

        }
        return resp;
    }

}
