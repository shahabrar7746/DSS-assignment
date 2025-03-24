package org.assignment.entities;

import org.assignment.enums.Currency;
import org.assignment.enums.OrderStatus;
import org.assignment.util.ColorCodes;

import java.time.LocalDateTime;

public class Order {
    private double price;

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return ColorCodes.YELLOW +
                "+-------------------+-------------------+-------------------+-------------------+-------------------+\n" +
                "| Order ID          | Product Name      | Status            | Ordered On        | Price             \n" +
                "+-------------------+-------------------+-------------------+-------------------+-------------------+\n" +
                String.format("| %-17s | %-17s | %-17s | %-17s | %-17.2f |\n",
                        id, product.getName(), status, orderedOn, price) +
                "+-------------------+-------------------+-------------------+-------------------+-------------------+\n" +
                ColorCodes.RESET;
    }

    public Long getId() {
        return id;
    }

    private Seller seller;

    public double getPrice() {
        return price;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
    private Currency currency;

    private Customer customer;
    private Product product;
    private Long id;
    private OrderStatus status;
    private LocalDateTime orderedOn;

    public void setId(Long id) {
        this.id = id;
    }

    public Order(Customer customer, Product product, Seller seller, OrderStatus status, Currency currency, LocalDateTime orderedOn, double price){
        this.currency = currency;
        this.orderedOn = orderedOn;
        this.customer = customer;
        this.price = price;
        this.seller = seller;
        this.product = product;
        this.status = status;
    }

    public LocalDateTime getOrderedOn() {
        return orderedOn;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public boolean equals(Object obj) {
        Order newOrder = (Order) obj;
        return this.getOrderId().equals(newOrder.getOrderId());
    }
    @Override
    public int hashCode() {
        Integer hash = (int) (this.getOrderId() + this.getCustomer().getId());//making hashcode on basis of order id and customer id.
        return hash.hashCode();
    }

    public Long getOrderId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Order() {
        this.customer = null;
    }
}