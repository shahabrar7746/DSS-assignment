package org.assignment.wrappers;


import org.assignment.entities.Product;
import org.assignment.enums.Currency;
import org.assignment.enums.ProductType;
import org.assignment.util.ColorCodes;

public class ProductWrapper {




    public ProductWrapper(Product product)
    {
        this.currency = product.getCurrency();
        this.name = product.getName();
        this.price = product.getPrice();
        this.type = product.getType();
    }

    private double price;


    private Currency currency;


    private String name;


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
