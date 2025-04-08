package com.foodorder.app.serviceImpl;



import com.foodorder.app.entities.FoodItem;
import com.foodorder.app.entities.Order;
import com.foodorder.app.entities.OrderItem;
import com.foodorder.app.entities.User;
import com.foodorder.app.enums.OrderStatus;
import com.foodorder.app.enums.UserRole;
import com.foodorder.app.service.CustomerService;
import com.foodorder.app.service.OrderService;

import java.util.HashMap;
import java.util.Map;

public class CustomerServiceImpl implements CustomerService {

    private Map<Integer, OrderItem> cart = new HashMap<>();
    private final User customer;
    private final OrderService orderService;

    public CustomerServiceImpl(User customer, OrderService orderService) {
        if (customer.getRole() != UserRole.CUSTOMER) {
            throw new IllegalArgumentException("User must be a customer.");
        }
        this.customer = customer;
        this.orderService = orderService;
    }

    public void addToCart(FoodItem foodItem, int quantity) {
        boolean itemExists = cart.values().stream()
                .anyMatch(item -> item
                        .getFood()
                        .getName()
                        .equalsIgnoreCase(foodItem.getName()));

        if (itemExists) {
            cart.values().stream()
                    .filter(item -> item.getFood()
                            .getName()
                            .equalsIgnoreCase(foodItem.getName()))
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