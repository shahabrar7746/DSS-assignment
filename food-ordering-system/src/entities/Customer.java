package entities;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Customer {
    private final int id;
    private final String name;
    private final String email;
    private final String password;
    private final Map<String, Food> cart;
    private double bill;
    private boolean isLoggedIn = false;

    public Customer(String email, String name, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.cart = new HashMap<>();
        this.id = new Random().nextInt(0, 2000);
    }
    public double getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }
    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public Map<String, Food> getCart() {
        return cart;
    }

    public void addToCart(String name, Food food) {// add implementation in serviceImp class
        cart.put(name, food);
    }

    public void updateCart(String foodItem, int newQuantity) {

    }
//    public void updateQuantity(Food food, int quantity) {// add implementation in serviceImp class
//        if (quantity <= 0) {
//            cart.remove(food);
//        } else {
//            cart.put(food, quantity);
//        }
//    }
//
//    public void removeFromCart(Food food, int quantity) {// add implementation in serviceImp class
//        int currentQuantity = cart.getOrDefault(food, 0);
//        if (currentQuantity > quantity) {
//            cart.put(food, currentQuantity - quantity);
//        } else {
//            cart.remove(food);
//        }
//    }

    public void clearCart() {
        cart.clear();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "email='" + email + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


}