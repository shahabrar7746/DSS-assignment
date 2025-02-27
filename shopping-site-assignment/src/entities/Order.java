package entities;

import enums.Currency;
import enums.OrderStatus;
import util.ColorCodes;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

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
    private Timestamp orderedOn;
    public Order(Long id,Customer customer, Product product, Seller seller, OrderStatus status,Currency currency, Timestamp timestamp, double price){
        this(customer, product, seller, price);
        this.currency = currency;
        this.orderedOn = timestamp;

        this.status = status;
        this.id = id;
    }
    public Order(Customer customer, Product product, Seller seller, double price) {
        this.currency = Currency.INR;
        this.customer = customer;
        this.price = price;
        this.seller = seller;
        this.product = product;
        this.status = OrderStatus.ORDERED;
        this.id = new Random().nextLong(0, 9000);
        this.orderedOn = new Timestamp(System.currentTimeMillis());
    }

    public Timestamp getOrderedOn() {
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