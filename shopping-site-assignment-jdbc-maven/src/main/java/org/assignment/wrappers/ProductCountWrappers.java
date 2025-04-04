package org.assignment.wrappers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ProductCountWrappers {
    private String productName;
    private int quantity;
    private double price;
    private double totalPrice;
    @Override
    public String toString() {
        return "+-----------------+----------+----------+------------+\n" +
                "| Product Name    | Quantity | Price      | Total Price |\n" +
                "+-----------------+----------+------------+------------+\n" +
                String.format("| %-15s | %-8d | %-10.2f | %-10.2f |\n",
                        productName, quantity, price, totalPrice) +
                "+-----------------+----------+------------+------------+";
    }


}
