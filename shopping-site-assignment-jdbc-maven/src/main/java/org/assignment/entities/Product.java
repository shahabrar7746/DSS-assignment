package org.assignment.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.assignment.enums.Currency;
import org.assignment.enums.ProductType;
import org.assignment.util.ColorCodes;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "price")
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @Column(name = "product_name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type")
    private ProductType type;

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
                "+------------------+------------+------------------+------------------+\n" +
                "| ProductName      | Price      | Currency         | Type             |\n" +
                "-+------------------+------------+------------------+-----------------+\n" +
                String.format("| %-16s | %-10.2f | %-16s | %-16s |\n",
                        name, price, currency, type) +
                "+-------------------+------------------+------------+-----------------+\n" +
                ColorCodes.RESET;
    }
}
