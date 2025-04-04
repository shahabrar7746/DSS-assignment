package org.assignment.entities;

import jakarta.persistence.*;
import lombok.*;
import org.assignment.enums.Currency;
import org.assignment.enums.OrderStatus;
import org.assignment.util.ColorCodes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


@Builder
@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "price")
    private double price;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "ordered_on")
    private LocalDateTime orderedOn;

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Order order = (Order) object;
        return Objects.equals(id, order.id);
    }
    @Column(name = "quantity")
    private int quantity;


    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
       return ColorCodes.YELLOW +
                "+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+\n" +
                "| Order ID          | Product Name      | Seller            | Customer          | Status            | Ordered On        | Price             | Quantity          |\n" +
                "+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+\n" +
                String.format("| %-17s | %-17s | %-17s | %-17s | %-17s | %-17s | %-17.2f | %-17d |\n",
                        id,
                        product != null ? product.getName() : "N/A",
                        seller != null ? seller.getName() : "N/A",
                        customer != null ? customer.getName() : "N/A",
                        status,
                        orderedOn,
                        price,
                        quantity) +
                "+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+\n" +
                ColorCodes.RESET;
    }
}