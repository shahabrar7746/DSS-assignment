package constants;

public enum RoomType {
    SINGLE(1000.0),
    DOUBLE(1800.0),
    SUITE(3000.0),
    DELUXE(5000.0);

    private final double pricePerNight;

    RoomType(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }
}
