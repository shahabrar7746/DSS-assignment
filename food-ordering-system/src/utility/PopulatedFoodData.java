package utility;

import entities.Food;
import enums.FoodCategory;
import java.util.ArrayList;
import java.util.List;

public class PopulatedFoodData {
    static List<Food> foodList = new ArrayList<>();

    static {
        Food food1 = new Food("Pasta", 456.6, FoodCategory.VEG);
        Food food2 = new Food("PaneerTikka", 200, FoodCategory.VEG);
        Food food3 = new Food("ChickenCurry", 456.6, FoodCategory.NONVEG);
        Food food4 = new Food("DumBiryani", 350, FoodCategory.NONVEG);
        Food food5 = new Food("Lassi", 100,FoodCategory.BEVERAGES);
        Food food6 = new Food("ButterMilk", 60,FoodCategory.BEVERAGES);
        foodList.add(food1);
        foodList.add(food2);
        foodList.add(food3);
        foodList.add(food4);
        foodList.add(food5);
        foodList.add(food6);
    }

    public static List<Food> getFoodItems() {
        return foodList;
    }
}