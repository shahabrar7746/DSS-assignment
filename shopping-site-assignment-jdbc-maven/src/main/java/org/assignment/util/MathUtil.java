package org.assignment.util;

public class MathUtil {
    private MathUtil(){}
    public  static double getTotalFromPriceAndQuantity(double price, int quantity) {
        return price * quantity;
    }
}
