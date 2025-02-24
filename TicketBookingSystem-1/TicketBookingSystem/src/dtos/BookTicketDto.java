package dtos;

import enums.Station;
import models.Passenger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookTicketDto {
    private int trainNumber;
    private Class<?> travellingClass;
    private Station from;
    private Station to;
    private LocalDate dateOfJourney;
    private List<Passenger> passengerList = new ArrayList<>();

    public BookTicketDto(int trainNumber, Class<?> travellingClass, Station from, Station to, LocalDate dateOfJourney, List<Passenger> passengerList) {
        this.trainNumber = trainNumber;
        this.travellingClass = travellingClass;
        this.from = from;
        this.to = to;
        this.dateOfJourney = dateOfJourney;
        this.passengerList = passengerList;
    }

    public BookTicketDto(int trainNumber, Class<?> travellingClass, Station from, Station to, LocalDate dateOfJourney) {
        this.trainNumber = trainNumber;
        this.travellingClass = travellingClass;
        this.from = from;
        this.to = to;
        this.dateOfJourney = dateOfJourney;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    public Class<?> getTravellingClass() {
        return travellingClass;
    }

    public void setTravellingClass(Class<?> travellingClass) {
        this.travellingClass = travellingClass;
    }

    public Station getFrom() {
        return from;
    }

    public void setFrom(Station from) {
        this.from = from;
    }

    public Station getTo() {
        return to;
    }

    public void setTo(Station to) {
        this.to = to;
    }

    public LocalDate getDateOfJourney() {
        return dateOfJourney;
    }

    public void setDateOfJourney(LocalDate dateOfJourney) {
        this.dateOfJourney = dateOfJourney;
    }

    public List<Passenger> getPassengerList() {
        return passengerList;
    }

    public void setPassengerList(List<Passenger> passengerList) {
        this.passengerList = passengerList;
    }
}
