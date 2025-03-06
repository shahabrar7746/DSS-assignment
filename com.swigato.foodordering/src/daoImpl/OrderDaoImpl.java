package daoImpl;

import dao.OrderDao;
import entities.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao
{
    private final List<Order> orders = new ArrayList<>();

    public void init(){

    }
    @Override
    public void addOrder(Order order) {
        orders.add(order);
    }

    @Override
    public Order getOrderById(int id) {
        return orders.stream().filter(o -> o.getId() == id).findFirst().orElse(null);
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