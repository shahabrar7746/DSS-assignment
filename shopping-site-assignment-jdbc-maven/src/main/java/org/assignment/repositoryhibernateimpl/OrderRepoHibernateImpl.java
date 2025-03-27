package org.assignment.repositoryhibernateimpl;

import jakarta.persistence.*;
import org.assignment.entities.Customer;
import org.assignment.entities.Order;
import org.assignment.entities.Product;
import org.assignment.enums.OrderStatus;
import org.assignment.exceptions.CustomerNotFoundException;
import org.assignment.exceptions.NoProductFoundException;
import org.assignment.exceptions.OrderNotFoundException;
import org.assignment.repository.interfaces.CustomerRepository;
import org.assignment.repository.interfaces.OrderRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class OrderRepoHibernateImpl implements OrderRepository {
    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory("myPersistenceUnit");
    private final EntityManager manager = factory.createEntityManager();
    private final EntityTransaction transaction = manager.getTransaction();
    private final String BASE_SELECTION_QUERY = "SELECT o FROM Order o ";
private final CustomerRepository customerRepository = CustomerRepoHibernateImpl.getInstance();
private static OrderRepoHibernateImpl singletonInstance;
private OrderRepoHibernateImpl(){}
public static OrderRepoHibernateImpl getInstance()
{
    if(singletonInstance == null){
        singletonInstance = new OrderRepoHibernateImpl();
    }
    return singletonInstance;
}
    @Override
    public List<Order> getAllOrders() throws SQLException, CustomerNotFoundException, NoProductFoundException {
        TypedQuery<Order> query = manager.createQuery(BASE_SELECTION_QUERY, Order.class);
        return query.getResultList();
    }

    @Override
    public Optional<Order> fetchOrderById(Long id) throws Exception {
        Order order = manager.find(Order.class, id);
        return order == null ? Optional.empty() : Optional.of(order);
    }

    @Override
    public void cancelOrder(Order order) throws SQLException {
        transaction.begin();
        manager.remove(order);
        transaction.commit();
    }

    @Override
    public Order addOrder(Order order) throws SQLException {
        transaction.begin();
        manager.persist(order);
        transaction.commit();
        return order;
    }

    @Override
    public List<Order> fetchOrderByProductAndCustomer(Product product, Customer customer) throws SQLException, CustomerNotFoundException, NoProductFoundException {
        String jpql = BASE_SELECTION_QUERY + " WHERE o.product = :product AND o.customer = :customer";
        TypedQuery<Order> query = manager.createQuery(jpql, Order.class);
        query.setParameter("product", product);
        query.setParameter("customer", customer);
        return query.getResultList();
    }

    @Override
    public List<Order> getOrderByCustomer(Customer customer) throws SQLException, CustomerNotFoundException, NoProductFoundException, OrderNotFoundException {
        String jpql = BASE_SELECTION_QUERY + " WHERE o.customer = :customer";
        TypedQuery<Order> query = manager.createQuery(jpql, Order.class);
        query.setParameter("customer", customer);
        List<Order> orders = query.getResultList();
        if(orders.isEmpty())
        {
            throw  new OrderNotFoundException("No Order found for the customer");
        }
        return orders;
    }

    @Override
    public List<Order> getAllDeliveredOrders() throws SQLException, CustomerNotFoundException, OrderNotFoundException, NoProductFoundException {
        String jpql = BASE_SELECTION_QUERY + " WHERE o.status = : status";
        TypedQuery<Order> query = manager.createQuery(jpql, Order.class);
        query.setParameter("status", OrderStatus.DELIVERED);
        List<Order> orders = query.getResultList();
        if(orders.isEmpty())
        {
            throw  new OrderNotFoundException("No Order found");
        }
        return orders;
    }
}
