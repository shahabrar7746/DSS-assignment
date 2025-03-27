package org.assignment.entities;

import jakarta.persistence.*;
import lombok.*;
import org.assignment.enums.Currency;
import org.assignment.enums.OrderStatus;
import org.assignment.util.ColorCodes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "orders")
@Builder
public class Order implements Serializable {
    private double price;
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
    @JoinColumn(name = "seller_id")
    @ManyToOne
    private Seller seller;
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "order_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;
@Column(name = "ordered_on")
    private LocalDateTime orderedOn;


    public Order(Customer customer, Product product, Seller seller, OrderStatus status, Currency currency, LocalDateTime orderedOn, double price) {
        this.currency = currency;
        this.orderedOn = orderedOn;
        this.customer = customer;
        this.price = price;
        this.seller = seller;
        this.product = product;
        this.status = status;
    }


    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Order order = (Order) object;
        return Objects.equals(id, order.id);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getOrderedOn() {
        return orderedOn;
    }

    public void setOrderedOn(LocalDateTime orderedOn) {
        this.orderedOn = orderedOn;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


}