package org.assignment.util;

import org.assignment.entities.CartItems;

import java.util.List;

public class PrintUtil {


    public static int getMaxRowLength(List<CartItems> cart) {
        int max = 0;
        for (CartItems item : cart) {
            int itemLength = item.getProduct().getName().length();
            max = Math.max(itemLength, max);
        }
        return max;
    }

    public static String generateLineNavDesignsFromCharAndInteger(char ch, int count)
    {
        String output = "";
        for (int i = 0; i < count+22; i++) {
            output += ch;
        }
        return output;
    }

}
