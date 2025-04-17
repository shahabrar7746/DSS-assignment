package org.assignment.entities;

import com.sun.source.tree.Tree;
import jakarta.persistence.*;
import lombok.*;
import org.assignment.enums.OrderStatus;
import org.assignment.util.ColorCodes;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;


@Builder
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order implements Serializable, Comparable<Order> {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "price")
    private double price;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User user;

    @JoinColumn(name = "ordered_product_id")
    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<OrderedProduct> orderedProducts;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;


    @CreationTimestamp
    @Column(name = "ordered_on")
    private LocalDateTime orderedOn;


    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

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

//    @Override
//    public String toString() {
//        return
//                String.format("| %-17s | %-17s | %-17s | %-17s | %-17s | %-17.2f | %-17s | %-17d |\n",
//                        product != null ? product.getName() : "N/A",
//                        product != null ? product.getSeller().getName() : "N/A",  // Assuming Product has a getSellerName() method
//                        user != null ? user.getName() : "N/A",
//                        status != null ? status.name() : "N/A",
//                        orderedOn != null ? orderedOn.toString() : "N/A",
//                        price,
//                       product != null ? product.getCurrency() : "N/A",  // Assuming Currency enum has a meaningful name
//                        quantity) +
//                "+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+";
//    }


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderCode='" + orderCode + '\'' +
                '}';
    }

    private String getPrintableOrderBody()
    {
        StringBuilder builder = new StringBuilder();
        for (OrderedProduct product : orderedProducts)
        {

        }
        return null;
    }

    @Override
    public int compareTo(Order o) {
        long result = this.id  - o.id;
        return (int) result;
    }
}