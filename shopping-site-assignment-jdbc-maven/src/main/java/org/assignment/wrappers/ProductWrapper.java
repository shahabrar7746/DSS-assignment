package org.assignment.wrappers;


import org.assignment.entities.Product;
import org.assignment.enums.Currency;
import org.assignment.enums.ProductType;
import org.assignment.util.ColorCodes;

public class ProductWrapper {


    private final String seller;
    private final double price;

    private final Currency currency;

    private final String name;

    private final ProductType type;

    public ProductWrapper(Product product) {
        this.currency = product.getCurrency();
        this.name = product.getName();
        this.price = product.getPrice();
        this.type = product.getType();
        this.seller = product.getSeller().getName();
    }


    @Override
    public String toString() {
        return ColorCodes.PURPLE +
                String.format("| %-16s | %-10.2f | %-16s | %-16s | %-16s |\n",
                        name, price, currency, type, seller) +
                "+-------------------+------------------+------------+-----------------+------------------+" +
                ColorCodes.RESET;
    }
}
