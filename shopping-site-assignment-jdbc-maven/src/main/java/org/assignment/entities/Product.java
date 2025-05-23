package org.assignment.entities;

import org.assignment.enums.Currency;
import org.assignment.enums.ProductType;
import org.assignment.util.ColorCodes;

public class Product {
    private double price;
    private Currency currency;
    private String name;

    public double getPrice() {
        return price;
    }

    public Currency getCurrency() {
        return currency;
    }

    private Long id;
    private ProductType type;
  //  private Seller seller;

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public ProductType getType() {
        return type;
    }

//    public Seller getSeller() {
//        return seller;
//    }
public Product(Long id, String name, Currency currency, double price, ProductType type){
    this.name = name;
    this.price = price;
    this.type = type;
    this.currency = currency;
        this.id = id;
}
    @Override
    public int hashCode() {

        return id.hashCode();
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
