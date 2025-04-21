package org.assignment.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ordered_product")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ordered_product_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private  Product product;


    @Column(name = "quantity", nullable = false)
    private  Integer quantity;

    @Column(name = "product_total", nullable = false, precision = 2)
    private double productTotal;
}
