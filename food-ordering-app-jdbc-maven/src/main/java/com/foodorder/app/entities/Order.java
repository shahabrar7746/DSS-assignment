
package com.foodorder.app.entities;


import com.foodorder.app.enums.OrderStatus;
import com.foodorder.app.utility.Formatable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order implements Formatable {
    private Integer id;
    private OrderStatus orderStatus;
    private User customer;
    private String orderOn; //todo
    private List<OrderItem> orderItems;

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOrderOn(String orderOn) {
        this.orderOn = orderOn;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }


    public Order(User customer, OrderStatus orderStatus) {
        this.id = java.util.concurrent.ThreadLocalRandom.current().nextInt(1, 2000);
        this.customer = customer;
        this.orderStatus = orderStatus;
        this.orderOn = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm a"));
        this.orderItems = new ArrayList<>();
    }

    public int getId() {
        return id;
    }


    public OrderStatus getOrderStatus() {
        return orderStatus;
    }


    public User getCustomer() {
        return customer;
    }


    public String getOrderOn() {
        return orderOn;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }

    public double getTotalBillAmount() {//todo
        double total = 0;
        for (OrderItem item : orderItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    @Override
    public List<String> fieldsToDisplay() {
        return List.of("Order id", "Order status", "Customer", "Order on");
    }

    @Override
    public List<String> getFieldValues() {
        return List.of(String.valueOf(this.id), String.valueOf(this.orderStatus), String.valueOf(this.customer.getName()),
                String.valueOf(this.orderOn));
    }

    @Override
    public String getDisplayabletitle() {
        return "Order History";
    }
}