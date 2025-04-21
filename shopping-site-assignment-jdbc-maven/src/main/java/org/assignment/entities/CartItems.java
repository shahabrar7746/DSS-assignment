package org.assignment.entities;

import jakarta.persistence.*;
import lombok.*;
import org.assignment.util.ColorCodes;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    @Column(name = "quantity", nullable = false)
    @EqualsAndHashCode.Exclude
    private int quantity;

    @Override
    public String toString() {
        return ColorCodes.BRIGHT_BLUE +
                String.format("| %-17s | %-17d | %-18.2f | %-10.2f |",
                        product != null ? product.getName() : "Unknown Product", // Get product name or default to "Unknown Product"
                        quantity,
                        product != null ? product.getPrice() : 0.0, // Get price or default to 0.0 if product is null
                        product != null ? product.getPrice() * quantity : 0.0) // Calculate total price or default to 0.0
                +
                "\n+-------------------+-------------------+--------------------+------------+" +
                ColorCodes.RESET;
    }


}
