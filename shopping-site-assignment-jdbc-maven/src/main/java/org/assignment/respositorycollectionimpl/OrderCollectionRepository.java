package org.assignment.respositorycollectionimpl;

import org.assignment.entities.Customer;
import org.assignment.entities.Order;
import org.assignment.entities.Product;
import org.assignment.enums.OrderStatus;
import org.assignment.exceptions.OrderNotFoundException;
import org.assignment.repository.interfaces.OrderRepository;


import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public  class OrderCollectionRepository implements OrderRepository {


    private  final  List<Order> orders;

    public OrderCollectionRepository(){
        orders = new ArrayList<>();
    }


    @Override
    public  List<Order> getAllOrders() {
        return orders;
    }
    @Override
    public  Optional<Order> fetchOrderById(Long id) {
       // Map<Long, Order> map = orders.stream().collect(Collectors.toConcurrentMap(Order::getOrderId, o -> o));
       // return map.containsKey(id) ? Optional.of(map.get(id)) : Optional.empty();
        return Optional.empty();
    }
    @Override
    public  void cancelOrder(Order order) {
        if (orders.contains(order)) {
            orders.remove(order);
        }

    }
    @Override
    public  Order addOrder(Order order) throws SQLException {
        order.setId(new Random().nextLong(0, 90000));
        orders.add(order);
        return order;
    }
    @Override
    public  List<Order> fetchOrderByProductAndCustomer(Product product, Customer customer)
    {
        List<Order> orderList = orders.stream().filter(o->o.getProduct().getId().equals(product) && o.getCustomer().getId().equals(customer.getId())).toList();
        return orderList;
    }

    @Override
    public List<Order> getOrderByCustomer(final Customer customer) {
        return orders.stream().filter(o-> o.getCustomer().getId().equals(customer.getId())).toList();
    }

    @Override
    public List<Order> getAllDeliveredOrders() throws OrderNotFoundException {

        List<Order> deliveredOrders = orders.stream().filter(o ->
            o.getStatus() == OrderStatus.DELIVERED

        ).toList();
        if (deliveredOrders.isEmpty()) {
            throw new OrderNotFoundException(deliveredOrders.size() + " orders are delivered");// throws exception if orders are delivered,
        }

        return deliveredOrders;
    }

    @Override
    public Order updateOrder(Order order) {
        return null;
    }

    @Override
    public List<Order> getOrdersByStatusAndCustomer(Customer customer, OrderStatus status) {
        return List.of();
    }


}
