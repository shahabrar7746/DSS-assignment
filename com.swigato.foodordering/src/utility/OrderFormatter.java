package utility;

import entities.Order;
import entities.OrderItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class OrderFormatter {

    public static String formatOrderDetails(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append(ColourCodes.BLUE+"Order ID: "+ColourCodes.RESET).append(order.getId()).append("\n");
        sb.append(ColourCodes.BLUE+"Status: "+ColourCodes.RESET).append(order.getOrderStatus()).append("\n");
        sb.append(ColourCodes.BLUE+"Customer: "+ColourCodes.RESET).append(order.getCustomer().getName()).append("\n");
        sb.append(ColourCodes.BLUE+"Order Time: "+ColourCodes.RESET).append(order.getOrderOn()).append("\n");
        sb.append(ColourCodes.BLUE+"Items:\n"+ColourCodes.RESET);

        List<OrderItem> items = order.getOrderItems();
        for (OrderItem item : items) {
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