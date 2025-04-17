package org.assignment.repositoryhibernateimpl;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.assignment.entities.Invoice;
import org.assignment.entities.User;
import org.assignment.entities.Order;
import org.assignment.entities.Product;
import org.assignment.enums.OrderStatus;

import org.assignment.exceptions.OrderNotFoundException;
import org.assignment.repository.interfaces.OrderRepository;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
public class OrderRepoHibernateImpl implements OrderRepository {

    private final  EntityManager manager;
    private final    EntityTransaction transaction;
    private static final String BASE_SELECTION_QUERY = "SELECT o FROM Order o";
    //Order o = new Order()

    @Override
    public List<Order> getAllOrders(){
        TypedQuery<Order> query = manager.createQuery(BASE_SELECTION_QUERY, Order.class);
        return query.getResultList();
    }

    @Override
    public Optional<Order> fetchOrderById(Long id)  {
        Order order = manager.find(Order.class, id);
        return Optional.ofNullable(order);
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
    public Optional<Order> fetchOrderByInvoiceAndUser(Invoice invoice, User user)  {
        String jpql = BASE_SELECTION_QUERY + " WHERE o.invoice = :product AND o.user = :user AND o.status = :status";
        TypedQuery<Order> query = manager.createQuery(jpql, Order.class);
        query.setParameter("invoice", invoice);
        query.setParameter("user", user);
        query.setParameter("status", OrderStatus.ORDERED);
       return Optional.ofNullable(query.getSingleResultOrNull());
    }

    @Override
    public List<Order> getOrderByCustomer(User user) throws OrderNotFoundException{
        String jpql = BASE_SELECTION_QUERY + " WHERE o.user = :user";
        TypedQuery<Order> query = manager.createQuery(jpql, Order.class);
        query.setParameter("user", user);
        List<Order> orders = query.getResultList();
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("No Order found for the user");
        }
        return orders;
    }

    @Override
    public List<Order> getAllDeliveredOrders() throws OrderNotFoundException {
        String jpql = BASE_SELECTION_QUERY + " WHERE o.status = :status";
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
    public List<Order> getOrdersByStatusAndCustomer(User user, OrderStatus status) {
        String jpql = BASE_SELECTION_QUERY + " WHERE o.user = :user AND o.status = :status";
        TypedQuery<Order> typedQuery = manager.createQuery(jpql, Order.class);
        typedQuery.setParameter("user", user);
        typedQuery.setParameter("status", status);
        return typedQuery.getResultList();
    }
}
