package models;

public class Coach {
     private String coachNumber;
    private double fair;
    private int total_seats = 80;
    private int availableSeats = 80;
    //    private int lower = 20;
    //    private int middle = 20;
    //    private int upper = 20;
    //    private int sideUpper = 10;
    //    private int sideLower = 10;


    public Coach(String coachNumber, double fair) {
        this.coachNumber = coachNumber;
        this.fair = fair;
    }

    public double getFair() {
        return this.fair;
    }

    public void setFair(double fair) {
        this.fair = fair;
    }

    public int getTotal_seats() {
        return this.total_seats;
    }

    public void setTotal_seats(int total_seats) {
        this.total_seats = total_seats;
    }

    public int getAvailableSeats() {
        return this.availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getCoachNumber() {
        return this.coachNumber;
    }

    public void setCoachNumber(String coachNumber) {
        this.coachNumber = coachNumber;
    }

    public boolean bookSeat(int quantity){
        if (quantity<=this.availableSeats){
            this.availableSeats = this.availableSeats-quantity;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.coachNumber;
    }
}

