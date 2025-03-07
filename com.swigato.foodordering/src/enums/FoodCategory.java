package enums;

public enum FoodCategory {
    VEG("VEG"), NONVEG("NONVEG"), BEVERAGES("BEVERAGES");

    private final String category;

    FoodCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }

}
