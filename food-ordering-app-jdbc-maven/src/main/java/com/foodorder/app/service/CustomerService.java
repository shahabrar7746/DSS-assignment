package com.foodorder.app.service;



import com.foodorder.app.entities.FoodItem;
import com.foodorder.app.entities.Order;
import com.foodorder.app.entities.OrderItem;

import java.util.Map;

public interface CustomerService {
    public void addToCart(FoodItem foodItem, int quantity);

    public Map<Integer, OrderItem> getCart();

    public void clearCart();

    public Order placeOrder();
}
