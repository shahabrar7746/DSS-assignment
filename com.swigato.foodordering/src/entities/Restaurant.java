package entities;

import exceptions.FailedToPerformOperation;
import exceptions.ValueAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

    public class Restaurant {
        private int id;
        private String name;
        private String email;
        private List<Integer> foodItemIds = new ArrayList<>();

        public Restaurant(int id, String name, String email, List<Integer> foodItemIds) {
            Random random = new Random();
            this.name = name;
            this.email = email;
            this.foodItemIds = foodItemIds;
            this.id = id;
        }
        public Restaurant(String name, String email, List<Integer> foodItemIds) {
            this.name = name;
            this.email = email;
            this.foodItemIds = foodItemIds;
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

        public List<Integer> getFoodItemIds() {
            return foodItemIds;
        }

        public void setFoodItemIds(List<Integer> foodItemIds) {
            this.foodItemIds = foodItemIds;
        }

//    public List<FoodItem> getFoodItemList() {
//        return fod;
//    }

//    public void setFoodItemList(List<FoodItem> foodItemItemList) {
//        this.foodItemItemList = foodItemItemList;
//    }

//    public void addFoodItem(FoodItem foodItem) throws FailedToPerformOperation, ValueAlreadyExistsException {
//        if (foodItemItemList.contains(foodItem)) {
//            throw new ValueAlreadyExistsException("Food already exists");
//        }
//
//        boolean success = foodItemItemList.add(foodItem);
//        if (!success) {
//            throw new FailedToPerformOperation("Unable to add food");
//        }
//        foodItemItemList.add(foodItem);
//    }

//    public boolean removeFoodItem(FoodItem foodItem) throws FailedToPerformOperation {
//        FoodItem itemToDelete = foodItemItemList.stream()
//                .filter(f -> f.getName().equals(foodItem.getName()))
//                .findFirst()
//                .orElseThrow(() -> new FailedToPerformOperation("Food item not found!"));
//
//        foodItemItemList.remove(itemToDelete);
//        return false;
//    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}