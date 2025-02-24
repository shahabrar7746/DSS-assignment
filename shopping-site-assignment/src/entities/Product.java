package entities;

import enums.Currency;
import enums.ProductType;
import util.ColorCodes;

import java.util.Random;

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
    private Seller seller;

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public ProductType getType() {
        return type;
    }

    public Seller getSeller() {
        return seller;
    }

    public Product(String name, Currency currency, double price, ProductType type, Seller seller) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.currency = currency;
        this.seller = seller;
        this.id = new Random().nextLong(0, 9000);
    }

    @Override
    public int hashCode() {
        Integer hash = (int) (this.getId() + this.seller.getId());
        return hash.hashCode();
    }

    @Override
    public String toString() {
        return ColorCodes.PURPLE +
                "+------------------+------------+------------------+------------------+-----------------+\n" +
                "| ProductName      | Price      | Currency         | Type             | Seller            |\n" +
                "-+------------------+------------+------------------+------------------+------------------+\n" +
                String.format("| %-16s | %-10.2f | %-16s | %-16s | %-17s |\n",
                        name, price, currency, type, seller.getName()) +
                "+-------------------+------------------+------------+------------------+------------------+\n" +
                ColorCodes.RESET;
    }
}
