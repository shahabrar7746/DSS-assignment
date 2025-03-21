package com.foodorder.app.daoImpl;


import com.foodorder.app.dao.OrderDao;
import com.foodorder.app.entities.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {

    private final List<Order> orders = new ArrayList<>();
    private final static OrderDaoImpl orderDao = new OrderDaoImpl();
    private OrderDaoImpl(){}

     public static OrderDaoImpl getOrderDao(){
        return orderDao ;
    }
    @Override
    public void addOrder(Order order) {
        orders.add(order);
    }

    @Override
    public Optional<Order> getOrderById(int id) {
        return orders.stream().filter(o -> o.getId() == id).findFirst();
    }

    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    @Override
    public void updateOrder(Order order) {
        orders.stream().filter(o -> o.getId() == order.getId()).findFirst().ifPresent(o -> {
            o.setOrderStatus(order.getOrderStatus());
            o.setCustomer(order.getCustomer());
            o.setOrderItems(order.getOrderItems());
        });
    }

    @Override
    public void deleteOrder(Order order) {
        orders.removeIf(o -> o.getId() == order.getId());
    }
}