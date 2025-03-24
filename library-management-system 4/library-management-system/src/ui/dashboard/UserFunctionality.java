package ui.dashboard;

import enums.BookCategory;

import java.util.Scanner;
import java.util.logging.Logger;


public class UserFunctionality {
    static Scanner sc = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(UserFunctionality.class.getName());


    /// userInput for bookBorrow and return



    public static void dispalyCategoryEnumValue() {
        System.out.print("Categories :[");
        for (BookCategory e : BookCategory.values()) {
            System.out.print(e + ",");
        }
        System.out.println("]");
    }


}
