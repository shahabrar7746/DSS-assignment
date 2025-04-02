package org.assignment.repositoryhibernateimpl;

import jakarta.persistence.*;
import org.assignment.entities.Customer;
import org.assignment.entities.Order;
import org.assignment.entities.Product;
import org.assignment.enums.OrderStatus;

import org.assignment.exceptions.OrderNotFoundException;
import org.assignment.repository.interfaces.OrderRepository;

import java.util.List;
import java.util.Optional;

public class OrderRepoHibernateImpl implements OrderRepository {
    private final  EntityManagerFactory factory = Persistence.createEntityManagerFactory("myPersistenceUnit");
    private final  EntityManager manager = factory.createEntityManager();
    private final  EntityTransaction transaction = manager.getTransaction();
    private final static String BASE_SELECTION_QUERY = "SELECT o FROM Order o ";

    @Override
    public List<Order> getAllOrders(){
        TypedQuery<Order> query = manager.createQuery(BASE_SELECTION_QUERY, Order.class);
        return query.getResultList();
    }

    @Override
    public Optional<Order> fetchOrderById(Long id)  {
        Order order = manager.find(Order.class, id);
        return order == null ? Optional.empty() : Optional.of(order);
    }

    @Override
    public void cancelOrder(Order order) {
        transaction.begin();
        manager.remove(order);
        transaction.commit();
    }

    @Override
    public Order addOrder(Order order) {
        transaction.begin();
        manager.persist(order);
        transaction.commit();
        return order;
    }

    @Override
    public List<Order> fetchOrderByProductAndCustomer(Product product, Customer customer)  {
        String jpql = BASE_SELECTION_QUERY + " WHERE o.product = :product AND o.customer = :customer AND o.status = :status";
        TypedQuery<Order> query = manager.createQuery(jpql, Order.class);
        query.setParameter("product", product);
        query.setParameter("customer", customer);
        query.setParameter("status", OrderStatus.ORDERED);
        return query.getResultList();
    }

    @Override
    public List<Order> getOrderByCustomer(Customer customer) throws OrderNotFoundException{
        String jpql = BASE_SELECTION_QUERY + " WHERE o.customer = :customer";
        TypedQuery<Order> query = manager.createQuery(jpql, Order.class);
        query.setParameter("customer", customer);
        List<Order> orders = query.getResultList();
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("No Order found for the customer");
        }
        return orders;
    }

    @Override
    public List<Order> getAllDeliveredOrders() throws OrderNotFoundException {
        String jpql = BASE_SELECTION_QUERY + " WHERE o.status = : status";
        TypedQuery<Order> query = manager.createQuery(jpql, Order.class);
        query.setParameter("status", OrderStatus.DELIVERED);
        List<Order> orders = query.getResultList();
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("No Order found");
        }
        return orders;
    }

    @Override
    public Order updateOrder(Order order) {
        transaction.begin();
        manager.merge(order);
        transaction.commit();
        return order;
    }

    @Override
    public List<Order> getOrdersByStatusAndCustomer(Customer customer, OrderStatus status) {
        String jpql = BASE_SELECTION_QUERY + "WHERE o.customer = :customer AND o.status = :status";
        TypedQuery<Order> typedQuery = manager.createQuery(jpql, Order.class);
        typedQuery.setParameter("customer", customer);
        typedQuery.setParameter("status", status);
        return typedQuery.getResultList();
    }
}
