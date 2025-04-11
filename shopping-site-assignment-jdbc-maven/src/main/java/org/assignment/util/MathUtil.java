package org.assignment.util;

public class MathUtil {
    private MathUtil(){}
    public  static double getTotalFromPriceAndQuantity(double price, int quantity) {
        return Double.valueOf(String.format("%.2f", price * quantity));
    }
}
