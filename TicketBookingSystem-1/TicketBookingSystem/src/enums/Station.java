package enums;

public enum Station {
    DELHI("Delhi"),
    MUMBAI("Mumbai"),
    KOLKATA("Kolkata"),
    BANGALORE("Bangalore"),
    CHENNAI("Chennai"),
    HYDERABAD("Hyderabad"),
    PUNE("Pune"),
    AHMEDABAD("Ahmedabad"),
    KANPUR("Kanpur"),
    LUCKNOW("Lucknow"),
    PATNA("Patna"),
    VARANASI("Varanasi"),
    JAIPUR("Jaipur"),
    INDORE("Indore"),
    CHANDIGARH("Chandigarh"),
    KOCHI("Kochi"),
    BHOJPUR("Bhojpur"),
    SURAT("Surat"),
    RANCHI("Ranchi"),
    BHUBANESWAR("Bhubaneswar");
    private final String stationName;

    Station(String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }

    public static Station fromString(String stationStr){
        try{
            return Station.valueOf(stationStr.toUpperCase());
        }catch (IllegalArgumentException E){
            return null;
        }
    }
}
