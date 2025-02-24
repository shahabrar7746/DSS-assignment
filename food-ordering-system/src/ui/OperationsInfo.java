package ui;

import java.util.List;

public abstract class OperationsInfo {
    public static void displayOperation(List<String> data){
        data.forEach(System.out::println);
    }
}
