package org.assignment.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.assignment.enums.Currency;
import org.assignment.enums.ProductType;
import org.assignment.util.ColorCodes;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeRegistration;
import org.hibernate.annotations.TypeRegistrations;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
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
