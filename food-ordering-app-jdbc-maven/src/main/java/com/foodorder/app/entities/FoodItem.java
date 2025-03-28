package com.foodorder.app.entities;

import com.foodorder.app.enums.FoodCategory;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class FoodItem {
    private Integer id;
    private String name;
    private double price;
    private FoodCategory category;
    private Integer restaurantId;
    private static final AtomicInteger nextFoodId = new AtomicInteger(1);

    public FoodItem(String name, double price, FoodCategory category, Integer restaurantId) {
        this.id = nextFoodId.getAndIncrement();
        this.name = name;
        this.price = price;
        this.category = category;
        this.restaurantId =restaurantId;
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