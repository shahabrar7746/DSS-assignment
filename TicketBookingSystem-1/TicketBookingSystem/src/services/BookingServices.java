package services;

import dtos.BookTicketDto;
import dtos.TrainSearchDto;
import enums.Station;
import models.Passenger;
import models.Transaction;
import models.User;
import utilpackage.Formatter;
import utilpackage.PopulateSomeHardCodedData;
import utilpackage.TrainProtoType;

import java.awt.print.Book;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingServices {
    private static List<List<Station>> routes = PopulateSomeHardCodedData.populateRoutes();
    private static Map<Integer, TrainProtoType> trains = PopulateSomeHardCodedData.populateTrain(routes);
    private static Map<Integer, Transaction> transactions = new HashMap<>();

    public int bookTicket(User user, BookTicketDto bookTicketDto){
        List<Passenger> passengers = bookTicketDto.getPassengerList();
        TrainProtoType train = trains.get(bookTicketDto.getTrainNumber());
        int availableSeats = train.getAvailableSeats(bookTicketDto.getTravellingClass());
        if(availableSeats>passengers.size()){
            passengers.forEach(passenger -> passenger.setSeatNumber(train.bookSeat(bookTicketDto.getTravellingClass())));
        }else{
            return -1;
        }
        Transaction transaction = new Transaction(bookTicketDto.getTrainNumber(), bookTicketDto.getFrom(), bookTicketDto.getTo(), bookTicketDto.getDateOfJourney(), passengers, user);
        int pnr = transaction.getPnr();
        user.getPnrList().add(pnr);
        transactions.put(pnr, transaction);
        return pnr;
    }

    public List<TrainProtoType> searchTrains(TrainSearchDto trainSearchData){
        List<TrainProtoType> filteredTrains = trains.values().stream()
                .filter(t -> t.getRoute().containsAll(List.of(Station.fromString(trainSearchData.getSource()), Station.fromString(trainSearchData.getDestination()))))
                .filter(t -> t.getRoute().indexOf(Station.fromString(trainSearchData.getSource())) < t.getRoute().indexOf(Station.fromString(trainSearchData.getDestination())))
                .toList();
        return filteredTrains;
    }

    public Transaction getTransaction(int pnr){
        return transactions.get(pnr);
    }

    public TrainProtoType getTrain(int trainNumber){
        return trains.get(trainNumber);
    }


}
