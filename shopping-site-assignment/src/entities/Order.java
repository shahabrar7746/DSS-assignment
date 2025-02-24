package entities;

import enums.OrderStatus;
import util.ColorCodes;

import java.util.Date;
import java.util.Random;

public class Order {
    @Override
    public String toString() {
        return ColorCodes.YELLOW +
                "+-------------------+-------------------+---------------------------------+-------------------+-------------------+\n" +
                "| Order ID          | Product Name      | Status            | Ordered On                      | Seller            |\n" +
                "+-------------------+-------------------+---------------------------------+-------------------+-------------------+\n" +
                String.format("| %-17s | %-17s | %-17s | %-17s | %-17s |\n",
                        id, product.getName(), status, orderedOn, product.getSeller().getName()) +
                "+-------------------+-------------------+---------------------------------+-------------------+-------------------+\n" +
                ColorCodes.RESET;
    }


    private Customer customer;
    private Product product;
    private Long id;
    private OrderStatus status;
    private Date orderedOn;

    public Order(Customer customer, Product product) {
        this.customer = customer;
        this.product = product;
        this.status = OrderStatus.ORDERED;
        this.id = new Random().nextLong(0, 9000);
        this.orderedOn = new Date();
    }

    public Date getOrderedOn() {
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