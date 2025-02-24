package enums;

public enum Day {
    MONDAY("MONDAY"), TUESDAY("TUESDAY"), WEDNESDAY("WEWDNESDAY"), THURSDAY("THURSDAY"), FRIDAY("FRIDAY"), SATURDAY("SATURDAY"), SUNDAY("SUNDAY");

    private final String day;
    Day(String day){
        this.day = day;
    }

    public String getDay(){
        return this.day;
    }

    public static Day fromString(String dayStr){
        try{
            return Day.valueOf(dayStr.toUpperCase());
        }catch (IllegalArgumentException E){
            return null;
        }
    }
}
