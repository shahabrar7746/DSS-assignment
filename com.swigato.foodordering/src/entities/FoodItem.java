package entities;

import enums.FoodCategory;

import java.util.Objects;

public class FoodItem {
    private int id;
    private String name;
    private double price;
    private FoodCategory category;

    public FoodItem(String name, double price, FoodCategory category) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return String.format("| %-15s | %-10.2f | %-13s |", name, price, category);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodItem foodItem = (FoodItem) o;
        return Double.compare(foodItem.price, price) == 0 && Objects.equals(name, foodItem.name) && category == foodItem.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, category);
    }


}