package service;

import entities.FoodItem;
import entities.Order;
import entities.OrderItem;

import java.util.Map;

public interface CustomerService {
    public void addToCart(FoodItem foodItem, int quantity);

    public Map<Integer, OrderItem> getCart();

    public void clearCart();

    public Order placeOrder();
}
