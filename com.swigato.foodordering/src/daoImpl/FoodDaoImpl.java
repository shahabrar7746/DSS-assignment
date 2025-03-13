package daoImpl;

import dao.FoodDao;
import entities.FoodItem;
import enums.FoodCategory;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FoodDaoImpl implements FoodDao {
    private final List<FoodItem> foodItems = new ArrayList<>();

    @Override
    public void addFood(FoodItem foodItem) {
        this.foodItems.add(foodItem);
    }

    @Override
    public Optional<FoodItem> getFoodById(int id) {
        Optional.of(foodItems.stream().fi)// TODO
        return foodItems.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<FoodItem> getAllFood() {
        return new ArrayList<>(foodItems);
    }

    @Override
    public List<FoodItem> getFoodByCategory(FoodCategory category) {
        return foodItems.stream()
                .filter(f -> f.getCategory() == category)
                .toList();
    }

    @Override
    public void updateFood(FoodItem foodItem) {
        foodItems.stream().filter(f -> f.getId() == foodItem.getId()).findFirst().ifPresent(f -> {
            f.setName(foodItem.getName());
            f.setPrice(foodItem.getPrice());
            f.setCategory(foodItem.getCategory());
        });
    }

    @Override
    public void deleteFood(FoodItem foodItem) { // TODO remove this
        this.foodItems.removeIf(f -> f.getId() == foodItem.getId());
    }
}