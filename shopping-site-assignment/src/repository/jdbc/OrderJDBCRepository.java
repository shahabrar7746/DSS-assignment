package repository.jdbc;

import entities.Order;
import repository.interfaces.OrderRepository;

import java.util.List;
import java.util.Optional;

public class OrderJDBCRepository implements OrderRepository {
    @Override
    public List<Order> getAllOrders() {
        return List.of();
    }

    @Override
    public Optional<Order> fetchOrderById(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean cancelOrder(Order order) {
        return false;
    }

    @Override
    public void addOrder(Order order) {

    }

    @Override
    public Optional<List<Order>> fetchOrderByProductName(String name) {
        return Optional.empty();
    }

    @Override
    public List<Order> getOrderByCustomerId(Long id) {
        return List.of();
    }
}
