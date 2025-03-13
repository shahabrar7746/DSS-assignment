package utility;

import entities.FoodItem;
import entities.Order;
import entities.OrderItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class OrderFormatter {

    public static String formatOrderDetails(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append(ColourCodes.BLUE+"Order ID: "+ColourCodes.RESET).append(order.getId()).append("\n");
        sb.append(ColourCodes.BLUE+"Status: "+ColourCodes.RESET).append(order.getOrderStatus()).append("\n");
        sb.append(ColourCodes.BLUE+"Customer: "+ColourCodes.RESET).append(order.getCustomer().getName()).append("\n");
        sb.append(ColourCodes.BLUE+"Order Time: "+ColourCodes.RESET).append(order.getOrderOn()).append("\n");
        sb.append(ColourCodes.BLUE+"Items:\n"+ColourCodes.RESET);

        List<OrderItem> items = order.getOrderItems();
        Predicate<FoodItem> predicate = (foodItem) -> {
            if(Objects.nonNull(foodItem.getName()) && )
          return true;
        }; // TODO
        items.stream().filter(r -> predicate.test(r)).forEach(item -> {

        });
        for (OrderItem item : items) { // traditional approach
            sb.append("  - ").append(item.getFood().getName()).append(ColourCodes.BLUE+" (Quantity: ,"+ColourCodes.RESET).append(item.getQuantity()).append(ColourCodes.BLUE+" Price: "+ColourCodes.RESET)
                    .append(roundToTwoDecimalPlaces(item.getTotalPrice())).append(")\n");
        }
        sb.append(ColourCodes.BLUE+"Total Bill Amount: "+ColourCodes.RESET).append(roundToTwoDecimalPlaces(order.getTotalBillAmount())).append("\n");

        return sb.toString();
    }

    private static double roundToTwoDecimalPlaces(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}