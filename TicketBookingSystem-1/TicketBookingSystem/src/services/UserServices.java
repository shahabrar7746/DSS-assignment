package services;

import dtos.BookTicketDto;
import dtos.CredentialDto;
import dtos.TrainSearchDto;
import models.*;
import utilpackage.PopulateSomeHardCodedData;
import utilpackage.TrainProtoType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserServices {
    private static BookingServices bookingServices = new BookingServices();
    private static Map<String, User> users = PopulateSomeHardCodedData.populateUsers();
    private static User currentUser;

    public static boolean register(User user){
        if (users.containsKey(user.getUserName())){
            return false;
        }
        return true;
    }

    public static int login(CredentialDto credential){
        User user = users.get(credential.getUsername());
        if(Objects.isNull(user)){
            return -1;
        }
        if(user.getPassword().equals(credential.getPassword())){
            user.setLoggedIn(true);
            currentUser = user;
            return 1;
        }else{
            return 0;
        }
    }

    public static List<TrainProtoType> searchTrains(TrainSearchDto trainSearchData){
        return bookingServices.searchTrains(trainSearchData);
    }

    public static void AddPassenger(Passenger passenger){
        currentUser.addPassenger(passenger);
    }

    public static int BookTicket(BookTicketDto bookTicketDto){
        if(currentUser.getIsLoggedIn()){
            return bookingServices.bookTicket(currentUser, bookTicketDto);
        }
        return -1;
    }

    public static void cancelTicket(int pnr){
        bookingServices.getTransaction(pnr).setCancelled(true);
    }

    public static ETicket getTickets(int pnr){
        Transaction transaction = bookingServices.getTransaction(pnr);
        ETicket eTicket = new ETicket(transaction);
        return eTicket;
    }

    public static TrainProtoType getTrain(int trainNumber) {
        return bookingServices.getTrain(trainNumber);
    }

    public static List<ETicket> getTickets(){
        return currentUser.getPnrList().stream().map(UserServices::getTickets).toList();
    }

    public static void logout(){
        currentUser.setLoggedIn(false);
        currentUser = null;
    }
}
