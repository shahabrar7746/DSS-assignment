package ui.display;

public abstract class DisplayOptions {
    public static void displayOption(String... data){
        for(String option:data){
            System.out.println(option);
        }
    }

}
