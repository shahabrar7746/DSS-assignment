package org.assignment.wrappers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ProductCountWrappers {
    private String productName;
    private int quantity;
    @Override
    public String toString() {
        return "+-----------------+----------+\n" +
                "| Product Name    | Quantity |\n" +
                "+-----------------+----------+\n" +
                String.format("| %-15s | %-8d |\n", productName, quantity) +
                "+-----------------+----------+";
    }

}
