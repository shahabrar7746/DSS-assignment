package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Restaurant {
    private int id;
    private String name;
    private String email;
    private List<Food> foodItemList; // initialize the variable to avoid NPE

    public Restaurant(String email, String name, List<Food> foodItemList) {
        this.email = email;
        this.name = name;
        this.foodItemList = foodItemList;
        this.id = new Random().nextInt(0, 2000);
    }

    public List<Food> getFoodItemList() {
        return foodItemList;
    }

    public List<Food> getFoodByCategory(String category) {
        List<Food> allFood = getFoodItemList();
        List<Food> filteredFood = new ArrayList<>();
        allFood.stream()
                .filter(food -> food.getCategory().getCategory().equalsIgnoreCase(category))
                .forEach(filteredFood::add);

        return filteredFood;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", foodItemList=" + foodItemList +
                '}';
    }
}