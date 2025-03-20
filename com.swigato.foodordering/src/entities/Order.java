package entities;

import enums.OrderStatus;
import utility.Formatable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order implements Formatable {
    private int id;
    private OrderStatus orderStatus;
    private User customer;
    private final String orderOn;
    private List<OrderItem> orderItems;

    public Order(User customer, OrderStatus orderStatus) {
        this.customer = customer;
        this.orderStatus = orderStatus;
        this.id = java.util.concurrent.ThreadLocalRandom.current().nextInt(1, 2000);
        this.orderOn = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm a"));
        this.orderItems = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
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

    public double getTotalBillAmount() {
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