package utility;

import java.util.List;

public class OperationsInfo {
    public static void displayMenu(List<String> strings) {
           strings.addLast("Choose an option: ");
        strings.forEach(System.out::println);
    }
}
