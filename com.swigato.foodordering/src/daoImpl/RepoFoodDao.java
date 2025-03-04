package daoImpl;

import dao.FoodDao;
import entities.FoodItem;
import enums.FoodCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepoFoodDao implements FoodDao {
    private final List<FoodItem> foodItems = new ArrayList<>();

    @Override
    public void addFood(FoodItem foodItem) {
        this.foodItems.add(foodItem);
    }

    @Override
    public FoodItem getFoodById(int id) {
        return foodItems.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<FoodItem> getAllFood() {
        return new ArrayList<>(foodItems);
    }


    @Override
    public List<FoodItem> getFoodByCategory(FoodCategory category) {
        return foodItems.stream().filter(f -> f.getCategory() == category).collect(Collectors.toList());
    }

    @Override
    public void updateFood(FoodItem foodItem) {
        this.foodItems.stream().filter(f -> f.getId() == foodItem.getId()).findFirst().ifPresent(f -> {
            f.setName(foodItem.getName());
            f.setPrice(foodItem.getPrice());
            f.setCategory(foodItem.getCategory());
        });
    }

    @Override
    public void deleteFood(FoodItem foodItem) {
        this.foodItems.removeIf(f -> f.getId() == foodItem.getId());
    }
}