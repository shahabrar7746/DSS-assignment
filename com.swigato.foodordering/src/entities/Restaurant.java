package entities;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private int id;
    private String name;
    private String email;
    private List<FoodItem> foodItemItemList;

    public Restaurant(String name, String email) {
        this.name = name;
        this.email = email;
        this.foodItemItemList = new ArrayList<>();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<FoodItem> getFoodItemList() {
        return foodItemItemList;
    }

    public void setFoodItemList(List<FoodItem> foodItemItemList) {
        this.foodItemItemList = foodItemItemList;
    }

    public void addFoodItem(FoodItem foodItem) {
        foodItemItemList.add(foodItem);
    }

    public void removeFoodItem(FoodItem foodItem) {
        foodItemItemList.remove(foodItem);
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", foodItemList=" + foodItemItemList +
                '}';
    }
}