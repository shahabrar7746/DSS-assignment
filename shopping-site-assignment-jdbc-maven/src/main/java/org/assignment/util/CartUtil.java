package org.assignment.util;

import org.assignment.entities.CartItems;

import java.util.List;

public class CartUtil {
    private CartUtil(){}
    public static double getCartTotal(List<CartItems> items)
    {
        double sum = 0.0;
        for (CartItems item : items)
        {
            sum += item.getProduct().getPrice() * item.getQuantity();
        }
        return sum;

    }
}
