package com.foodorder.app.utility;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class OperationsInfo {
    public static void displayMenu(String title, List<String> options) {
        System.out.println(ColourCodes.CYAN + "\n" + title.toUpperCase() + ColourCodes.RESET);
        System.out.println(ColourCodes.CYAN + "-----------------------------------------" + ColourCodes.RESET);

        AtomicInteger i = new AtomicInteger(1);
        options.forEach(option ->
                System.out.println("Choose option: " + i.getAndIncrement() + " ->" + option)
        );
        System.out.println(ColourCodes.CYAN + "-----------------------------------------"+ ColourCodes.RESET);
    }
}
