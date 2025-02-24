package controllers;

import dtos.BookTicketDto;
import dtos.CredentialDto;
import dtos.TrainSearchDto;
import enums.Station;
import models.ETicket;
import models.Passenger;
import models.Train;
import models.User;
import services.UserServices;
import utilpackage.Formatter;
import utilpackage.TrainProtoType;
import utilpackage.UtilClass;

import javax.swing.*;
import java.time.LocalDate;
import java.util.*;

public class UserController {
    private UserServices services = new UserServices();
    private static Scanner input = UtilClass.scanner;

    public void register(){
        System.out.print("Enter Name: ");
        String name = input.nextLine();
        System.out.print("Enter Age: ");
        int age = input.nextInt();
        System.out.print("Enter UserName: ");
        String userName = input.nextLine();
        System.out.print("Enter Password: ");
        String password = input.nextLine();
        services.register(new User(name, age, userName, password));
    }

    public int login(){
        System.out.print("Enter UserName: ");
        String userName = input.nextLine();
        System.out.print("Enter Password: ");
        String password = input.nextLine();
        int login = services.login(new CredentialDto(userName, password));
        if (login==1){
            System.out.println("Logged in.");
        } else if (login==-1) {
            System.out.println("User not found");
        } else if (login==0) {
            System.out.println("wrong password");
        }
        return login;
    }

        public void searchTrains(){
        System.out.print("Enter Source: ");
        String source = input.nextLine();
        System.out.print("Enter Destination: ");
        String destination = input.nextLine();
        System.out.println(source+destination);
        TrainSearchDto trainSearchDto = new TrainSearchDto(source, destination);
        List<TrainProtoType> filteredTrains = services.searchTrains(trainSearchDto);
        Formatter.tableTemplate(filteredTrains);
    }

    public static List<Passenger> addPassenger(){
        if(input.hasNextLine()){
            input.nextLine();
        }
        List<Passenger> passengers = new ArrayList<>();
        System.out.println("Add passengers: ");
        boolean flag = true;
        while(flag){
            System.out.println("Enter Name");
            String name = input.nextLine();
            System.out.println("Enter Age");
            int age = input.nextInt();
            passengers.add(new Passenger(name, age));
            if (input.hasNextLine()){
                input.nextLine();
            }
            System.out.println("Do you want to continue adding passenger ? y/n: ");
            String userChoice = input.nextLine();
            if(userChoice.toLowerCase().equals("n")) flag = false;
        }
        return passengers;
    }

    public int bookTicket(){
        System.out.print("Enter Source: ");
        String source = input.nextLine();
        System.out.print("Enter Destination: ");
        String destination = input.nextLine();
        System.out.print("Enter Date Of Journey: ");
        String date = input.nextLine();
        LocalDate doj = LocalDate.parse(date);
        List<TrainProtoType> availableTrains = services.searchTrains(new TrainSearchDto(source, destination));
        Formatter.tableTemplate(availableTrains);
        System.out.print("Enter Train Number: ");
        int trainNumber = input.nextInt();
        TrainProtoType train = services.getTrain(trainNumber);
        train.getAvailableCoacheClasses().forEach((index, coach)-> System.out.printf("[%d] %s\n", index, coach.toString()));
        System.out.print("Select Travelling Class: ");
        int travellingClassindex = input.nextInt();
        Class<?> travellingClass = train.getAvailableCoacheClasses().get(travellingClassindex);
        System.out.printf("Available Seats: %d\n", train.getAvailableSeats(travellingClass));
        List<Passenger> passengers = UserController.addPassenger();
        BookTicketDto bookTicketDto = new BookTicketDto(trainNumber, travellingClass, Station.fromString(source), Station.fromString(destination), doj, passengers);
        return services.BookTicket(bookTicketDto);
    }

    public void cancelTicket(){
        System.out.print("Enter PNR: ");
        int pnr = input.nextInt();
        services.cancelTicket(pnr);
    }

    public void viewTickets(){
        if(input.hasNext()){
            input.nextLine();
        }
        List<ETicket> tickets = UserServices.getTickets();
        for(ETicket ticket: tickets){
            System.out.println(ticket.toString());
        }
    }

    public void logout(){
        services.logout();

    }
}
