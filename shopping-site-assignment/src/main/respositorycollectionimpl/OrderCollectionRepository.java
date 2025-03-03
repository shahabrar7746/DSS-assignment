package main.respositorycollectionimpl;

import main.entities.Order;
import main.enums.OrderStatus;
import main.exceptions.OrderNotFoundException;
import main.repository.interfaces.OrderRepository;


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
        Map<Long, Order> map = orders.stream().collect(Collectors.toConcurrentMap(Order::getOrderId, o -> o));
        return map.containsKey(id) ? Optional.of(map.get(id)) : Optional.empty();
    }
    @Override
    public  boolean cancelOrder(Order order) {
        if (orders.contains(order)) {
            orders.remove(order);
            return true;
        }
        return false;
    }
    @Override
    public  void addOrder(Order order) {
        order.setId(new Random().nextLong(0, 90000));
        orders.add(order);
    }
    @Override
    public  List<Order> fetchOrderByProductName(String name)
    {
        List<Order> orderList = orders.stream().filter(o->o.getProduct().getName().equalsIgnoreCase(name)).toList();
        return orderList;
    }

    @Override
    public List<Order> getOrderByCustomerId(Long id) {
        return orders.stream().filter(o-> o.getCustomer().getId().equals(id)).toList();
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


}
