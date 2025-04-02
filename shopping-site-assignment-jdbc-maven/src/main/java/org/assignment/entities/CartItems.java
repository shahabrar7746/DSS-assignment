package org.assignment.entities;

import jakarta.persistence.*;
import lombok.*;
import org.assignment.util.ColorCodes;

import java.util.Objects;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_items")
@EqualsAndHashCode
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @EqualsAndHashCode.Exclude
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    @EqualsAndHashCode.Exclude
    private  Seller seller;

    @Column(name = "quantity")
    @EqualsAndHashCode.Exclude
    private int quantity;

    @Override
    public String toString() {
        return ColorCodes.BRIGHT_BLUE +
                "+-------------------+-------------------+\n" +
                "| Product Name      | Quantity          |\n" +
                "+-------------------+-------------------+\n" +
                String.format("| %-17s | %-17d |\n",
                        product != null ? product.getName() : "Unknown Product", // Get product name or default to "Unknown Product"
                        quantity) +
                "+-------------------+-------------------+\n" +
                ColorCodes.RESET;
    }

}
