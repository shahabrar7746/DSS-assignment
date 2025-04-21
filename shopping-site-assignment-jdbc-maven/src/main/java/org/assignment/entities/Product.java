package org.assignment.entities;

import jakarta.persistence.*;
import lombok.*;
import org.assignment.enums.Currency;
import org.assignment.enums.ProductType;
import org.assignment.util.ColorCodes;

import java.util.Objects;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false, precision = 2)
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;



    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false)
    private ProductType type;


    @Column(name = "available_quantity")
    private int stock;

    @JoinColumn(name = "seller_id")
    @ManyToOne
    private User seller;
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Product product = (Product) object;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return ColorCodes.PURPLE +
         String.format("| %-16s | %-10.2f | %-16s | %-16s |",
                name, price, currency, type)
                + ColorCodes.RESET;
    }
}
