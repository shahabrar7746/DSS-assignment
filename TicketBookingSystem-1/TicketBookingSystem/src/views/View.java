package views;

import dtos.CredentialDto;
import dtos.TrainSearchDto;
import models.Passenger;
import models.User;
import utilpackage.Formatter;

import java.util.*;

public class View {
    private Scanner input;

    public View(Scanner input) {
        this.input = input;
    }

    public User registerUserView() {
        if (input.hasNextLine()) {
            input.nextLine();
        }
        System.out.print("Enter Name: ");
        String name = input.nextLine();
        System.out.print("Enter Age: ");
        int age = input.nextInt();
        System.out.print("Enter UserName: ");
        String username = input.nextLine();
        System.out.print("Enter Password: ");
        String password = input.nextLine();

        return new User(name, age, username, password);
    }

    public CredentialDto LoginUserView() {
        if (input.hasNextLine()) {
            input.nextLine();
        }
        System.out.print("Enter UserName: ");
        String username = input.nextLine();
        System.out.print("Enter Password: ");
        String password = input.nextLine();

        return new CredentialDto(username, password);
    }

    public TrainSearchDto searchTrainsView(){
        if (input.hasNextLine()) {
            input.nextLine();
        }
        System.out.print("Enter Source: ");
        String source = input.nextLine();
        System.out.print("Enter Destination: ");
        String destination = input.nextLine();

        return new TrainSearchDto(source,destination);
    }

    public void bookTicketView(){
        if (input.hasNextLine()) {
            input.nextLine();
        }
        System.out.print("Enter Train Number: ");

        int trainNumber = input.nextInt();
        System.out.println("[1] 1A\n [2] 2A\n [3] 3A\n [4] Sleeper\n[5] Second Sitting\n[6] General\n[0] Exit");
        System.out.print("select coach class: ");
        byte coachClass = input.nextByte();
        if (coachClass== 1){

        } else if (coachClass==2) {

        } else if (coachClass==3) {

        } else if (coachClass==4) {

        } else if (coachClass==5) {

        } else if (coachClass==6) {

        } else if (coachClass==0) {
            return;
        }else{
            System.out.println("Please select a valid coach class.");
        }


    }

    public int getTrainNumberView(){
//        if (input.hasNextLine()) {
//            input.nextLine();
//        }
        System.out.print("Enter Train Number: ");
        int trainNumber = input.nextInt();
        return trainNumber;
    }

    public int getCoachClass(List<String> coaches){
        if (input.hasNextLine()) {
            input.nextLine();
        }
        for(int i=0;i<coaches.size();i++){
            System.out.printf("[%d] %s\n",i, coaches.get(i));

        }
        System.out.print("Select coach: ");
        int coachClass = input.nextInt();
        return coachClass;
    }

    public void displayAvailableSeats(int seat){
        System.out.printf("Available Seats: %d\n", seat);
    }

    public ArrayList<Passenger> getPassengersView(){
        if (input.hasNextLine()) {
            input.nextLine();
        }
        ArrayList<Passenger> passengers = new ArrayList<>();
        System.out.println("\nAdd passengers");
        while(true){
            System.out.print("Enter Name: ");
            String name = input.nextLine();
            System.out.print("Enter Age: ");
            int age = input.nextInt();
            System.out.println(name+age);
            passengers.add(new Passenger(name, age));
            System.out.print("Want to continue adding passenger ? y/n: ");
            if (input.hasNextLine()) {
                input.nextLine();
            }
            String option = input.nextLine();
            if(option.toLowerCase().equals("n")){
                Formatter.tableTemplate(passengers);
                return passengers;
            }
            if(! option.toLowerCase().equals("y")){
                System.out.println("Please enter a valid input.");
            }
        }
    }
}
