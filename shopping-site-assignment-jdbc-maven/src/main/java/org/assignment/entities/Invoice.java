package org.assignment.entities;

import jakarta.persistence.*;
import lombok.*;
import org.assignment.enums.Currency;
import org.assignment.enums.InvoicePrintStatus;
import org.assignment.util.MathUtil;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoice")

public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id")
    private Order order;

    @CreationTimestamp
    @Column(name = "generated_on")
    private LocalDateTime generatedOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "print_status")
    private InvoicePrintStatus printStatus;

    @Override
    public String toString() {
       StringBuilder stringBuilder = new StringBuilder(banner + header);
        for (OrderedProduct orderedProduct : order.getOrderedProducts()) {
            Product product = orderedProduct.getProduct();
            char currency = product.getCurrency().getSymbol();
            int quantity = orderedProduct.getQuantity();
           stringBuilder.append(PRODUCT_NAME + product.getName() + "\n")
                   .append(QUANTITY + quantity + "\n")
                   .append(PRICE_PER_QUANTITY + product.getPrice() + currency + "\n" )
                   .append(TOTAL_PRODUCT_PRICE + MathUtil.getTotalFromPriceAndQuantity(product.getPrice(), quantity) + currency + "\n")
                   .append(FOOTER);
        }
        return stringBuilder.toString();
    }

    @Transient
    static final String FOOTER = "====================================================\n";
    @Transient
    static final String PRODUCT_NAME = "Product name         : ";
    @Transient
    static final String QUANTITY = "Quantity             : ";
    @Transient
    static final String PRICE_PER_QUANTITY = "Price per quantity   : ";
    @Transient
    static final String TOTAL_PRODUCT_PRICE = "Total Product Amount : ";
    @Transient
    static final String banner = "======================INVOICES======================\n";
    @Transient
    static final String header = "======================PRODUCTS======================\n\n";
}
