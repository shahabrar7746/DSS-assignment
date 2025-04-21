package org.assignment.util;


import java.util.Scanner;


public class Inputs {


    public static int getIntegerInput(Scanner sc) {
        int num = sc.nextInt();
            sc.nextLine();
        return num;
    }

    public static String getStringInputs(Scanner sc) {
        String input  = sc.nextLine();
        return input;
    }

    public static double getDoubleInput(Scanner sc)
    {
        return sc.nextDouble();
    }
    public static long getLongInput(Scanner sc)
    {
        return sc.nextLong();
    }

}
