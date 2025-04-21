package org.assignment.entities;

import com.sun.source.tree.Tree;
import jakarta.persistence.*;
import lombok.*;
import org.assignment.enums.OrderStatus;
import org.assignment.util.ColorCodes;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order implements Serializable, Comparable<Order> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "grand_total", nullable = false, precision = 2)
    private double price;


    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false, updatable = false)
    private User user;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<OrderedProduct> orderedProducts;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;


    @CreationTimestamp
    @Column(name = "ordered_on", nullable = false, updatable = false)
    private LocalDateTime orderedOn;

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Order order = (Order) object;
        return Objects.equals(id, order.id);
    }



    @Transient
    public final String orderCode = "ORD" + new Random().nextInt(1000, 900000);


    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderCode='" + orderCode + '\'' +
                '}';
    }
    @Override
    public int compareTo(Order o) {
        long result = this.id  - o.id;
        return (int) result;
    }
}