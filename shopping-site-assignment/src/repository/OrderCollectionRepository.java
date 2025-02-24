package repository;

import entities.Customer;
import entities.Order;
import repository.interfaces.OrderRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
            Customer customer = order.getCustomer();
            customer.cancelOrder(order);
            return true;
        }
        return false;
    }
    @Override
    public  void addOrder(Order order) {
        orders.add(order);
    }
    @Override
    public  Optional<List<Order>> fetchOrderByProductName(String name)
    {
        List<Order> orderList = orders.stream().filter(o->o.getProduct().getName().equalsIgnoreCase(name)).toList();
        return orderList.isEmpty() ? Optional.empty() : Optional.of(orderList);
    }


}
