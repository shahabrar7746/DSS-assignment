package controllers;

import dtos.TrainSearchDto;
import enums.Station;
import dtos.CredentialDto;
import models.Passenger;
import models.Transaction;
import models.User;
import utilpackage.Formatter;
import utilpackage.PopulateSomeHardCodedData;
import utilpackage.TrainProtoType;
import views.View;

import java.util.*;

public class Controller {
    private static Map<String, User> users;
    private static List<List<Station>> routes;
    private static Map<Integer, TrainProtoType> trains;
    private static Map<Integer, Transaction> transactions = new HashMap<>();
    private static final Scanner input;
    private View view = new View(input);
    static {
        routes = PopulateSomeHardCodedData.populateRoutes();
        trains = PopulateSomeHardCodedData.populateTrain(routes);
        users = PopulateSomeHardCodedData.populateUsers();
        input =  new Scanner(System.in);
    }



    public void registerUser() {
        User user = view.registerUserView();
        if (users.containsKey(user.getUserName())){
            System.out.println("User with username already exists. Please try another username");
            return;
        }
        users.put(user.getUserName(), user);
        System.out.println("new user has been registered.");
    }

    public void loginUser() {
        CredentialDto credential = view.LoginUserView();
        User user = users.get(credential.getUsername());
        if(Objects.isNull(user)){
            System.out.println("No user with that username found.");
            return;
        }
        if(user.getPassword().equals(credential.getPassword())){
            user.setLoggedIn(true);
            System.out.println("user logged in successfully.");
            this.displayUserInterface();
        }else{
            System.out.println("Wrong Password");
        }
    }

    public boolean searchTrains(){
        TrainSearchDto trainSearchData = view.searchTrainsView();
        List<TrainProtoType> filteredTrains = trains.values().stream()
                .filter(t -> t.getRoute().containsAll(List.of(Station.fromString(trainSearchData.getSource()), Station.fromString(trainSearchData.getDestination()))))
                .filter(t -> t.getRoute().indexOf(Station.fromString(trainSearchData.getSource())) < t.getRoute().indexOf(Station.fromString(trainSearchData.getDestination())))
                .toList();
        Formatter.tableTemplate(filteredTrains);
        return ! filteredTrains.isEmpty();
    }

    public TrainProtoType getTrain(int trainNumber){
        return trains.get(trainNumber);
    }

    public void displayAllUsers(){
        Formatter.tableTemplate(users.values().stream().toList());
    }

    public void displayAllTrains(){
        Formatter.tableTemplate(trains.values().stream().toList());
    }

    public void addTrain(){

    }

    public void bookTicket(){


    }

    public void displayUserInterface(){
        while(true){
            System.out.println("Options");
            System.out.println("[1] Search Trains\n[2] Book Tickets\n[3] View Tickets\n[4] Cancel Ticket\n[5] Show Travelling History\n[0] Exit");
            System.out.print("Option :");
            byte option = input.nextByte();
            if(option==1){
                this.searchTrains();
            } else if (option==2) {
                this.bookTicket();
            } else if (option==3) {

            } else if (option==4) {

            } else if (option==5) {

            } else if (option==0) {
                    break;
            }else{
                System.out.println("Please select a valid option.");
            }
        }

    }

    public void addPassengers(){
        List<Passenger> passengers;


    }

    public void displayHome(){
        System.out.println("Options");
        System.out.println("[1] Login as Admin\n[2] Login as User\n[3] Register new User\n[4] Search trains\n[5] display all trains\n[0] Exit");
        System.out.print("Option :");
        byte option = input.nextByte();
        if(option==1){

        } else if (option==2) {
            this.loginUser();
        } else if (option==3) {
            this.registerUser();
        } else if (option==4) {
            this.searchTrains();
        } else if (option==5) {
            displayAllTrains();
        } else if (option==0) {
            System.out.println("Thank you for using Train Booking System.");
            System.exit(0);

        }else{
            System.out.println("Please select a valid option.");
        }

    }
}
