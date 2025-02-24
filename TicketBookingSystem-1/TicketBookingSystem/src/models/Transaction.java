package models;

import enums.Station;
import intefaces.Formatable;

import java.time.LocalDate;
import java.util.List;

public class Transaction implements Formatable {
    private final int pnr = (int) (Math.random()*1000000000);
    private int trainNumber;
    private Station source;
    private Station destination;
    private LocalDate dateOfJourney;
    private List<Passenger> passengers;
    private User user;
    private boolean isCancelled;

    public Transaction(int trainNumber, Station source, Station destination,LocalDate dateOfJourney,  List<Passenger> passengers, User user) {
        this.trainNumber = trainNumber;
        this.source = source;
        this.destination = destination;
        this.dateOfJourney = dateOfJourney;
        this.passengers = passengers;
        this.user = user;
    }

    public int getPnr() {
        return this.pnr;
    }

    public int getTrainNumber() {
        return this.trainNumber;
    }

    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    public Station getSource() {
        return this.source;
    }

    public void setSource(Station source) {
        this.source = source;
    }

    public Station getDestination() {
        return this.destination;
    }

    public void setDestination(Station destination) {
        this.destination = destination;
    }


    public LocalDate getDateOfJourney() {
        return dateOfJourney;
    }

    public void setDateOfJourney(LocalDate dateOfJourney) {
        this.dateOfJourney = dateOfJourney;
    }

    public List<Passenger> getPassenger() {
        return this.passengers;
    }

    public void setPassenger(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    @Override
    public List<String> fieldsToDisplay() {
        return List.of("PNR", "TrainNumber", "Source", "Destination", "Passenger", "User", "SeatNumber", "cancelled");
    }

    @Override
    public List<String> getFieldValues() {
        return List.of(String.valueOf(this.pnr), String.valueOf(this.trainNumber), this.source.getStationName(), this.destination.getStationName(), this.passengers.toString(), this.user.getName(), String.valueOf(isCancelled));
    }

    @Override
    public String getDisplayabletitle() {
        return "Transactions";
    }
}
