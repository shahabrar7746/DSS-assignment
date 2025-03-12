package entities;

import utility.Formatable;

import java.util.List;
import java.util.Random;

public class OrderItem implements Formatable {
    private int id;
    private FoodItem foodItem;
    private int quantity;

    public OrderItem(FoodItem foodItem, int quantity) {
        Random random = new Random();
        this.id = random.nextInt(1000);
        this.foodItem = foodItem;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FoodItem getFood() {
        return foodItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return foodItem.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "foodItem=" + foodItem +
                ", id=" + id +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public List<String> fieldsToDisplay() {
        return List.of("Name", "Price", "Category", "Quantity");
    }

    @Override
    public List<String> getFieldValues() {
        return List.of(this.foodItem.getName(), String.valueOf(this.foodItem.getPrice()), String.valueOf(this.foodItem.getCategory()), String.valueOf(this.quantity));
    }

    @Override
    public String getDisplayabletitle() {
        return "Ordered item";
    }
}