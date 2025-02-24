package entities;

import enums.FoodCategory;

import java.util.Random;

public class Food {
    private String name;
    private int id;
    private double price;
    private FoodCategory category;
    private Integer quantity;

    public Food(String name, double price, FoodCategory category) {
        this.id = new Random().nextInt(0, 2000);
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public FoodCategory getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object obj) { ///  update equals to all attributes
        Food foodObj = (Food) obj;
        return foodObj.name.equals(this.name);
    }

    public String toString() {
        return String.format("| %-10.2f | %-15s | %-10s |", price, name, category);
    }
}