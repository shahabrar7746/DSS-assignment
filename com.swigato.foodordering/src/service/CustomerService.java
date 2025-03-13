package service;

import entities.FoodItem;
import entities.Order;
import entities.OrderItem;
import entities.User;
import enums.OrderStatus;
import enums.UserRole;

import java.util.HashMap;
import java.util.Map;

public class CustomerService {

    private Map<Integer, OrderItem> cart = new HashMap<>();
    private final User customer;
    private final OrderService orderService;

    public CustomerService(User customer, OrderService orderService) {
        if (customer.getRole() != UserRole.CUSTOMER) {
            throw new IllegalArgumentException("User must be a customer.");
        }
        this.customer = customer;
        this.cart = new HashMap<>();
        this.orderService = orderService;
    }

    public void addToCart(FoodItem foodItem, int quantity) {
        boolean itemExists = cart.values().stream()
                .anyMatch(item -> item.getFood().getName().equalsIgnoreCase(foodItem.getName()));

        if (itemExists) {
            cart.values().stream()
                    .filter(item -> item.getFood().getName().equalsIgnoreCase(foodItem.getName()))
                    .forEach(item -> item.setQuantity(item.getQuantity() + quantity));
        } else {
            OrderItem orderItem = new OrderItem(foodItem, quantity);
            cart.put(orderItem.getId(), orderItem);
        }
    }

    public Map<Integer, OrderItem> getCart() {
        return cart;
    }

    public void clearCart() {
        cart.clear();
    }

    public Order placeOrder() {
        Order order = new Order(customer, OrderStatus.ORDERED);
        cart.values().forEach(order::addOrderItem);
        orderService.placeOrder(order);
        clearCart();
        return order;
    }

}