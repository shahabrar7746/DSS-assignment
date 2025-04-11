package org.assignment.entities;

import jakarta.persistence.*;
import lombok.*;
import org.assignment.enums.OrderStatus;
import org.assignment.util.ColorCodes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


@Builder
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "price")
    private double price;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User user;

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
        return
                String.format("| %-17s | %-17s | %-17s | %-17s | %-17s | %-17.2f | %-17s | %-17d |\n",
                        product != null ? product.getName() : "N/A",
                        product != null ? product.getSeller().getName() : "N/A",  // Assuming Product has a getSellerName() method
                        user != null ? user.getName() : "N/A",
                        status != null ? status.name() : "N/A",
                        orderedOn != null ? orderedOn.toString() : "N/A",
                        price,
                       product != null ? product.getCurrency() : "N/A",  // Assuming Currency enum has a meaningful name
                        quantity) +
                "+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+";
    }
}