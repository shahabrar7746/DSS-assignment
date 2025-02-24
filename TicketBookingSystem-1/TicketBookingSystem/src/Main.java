import controllers.Controller;
import controllers.MainController;
import controllers.UserController;
import models.User;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
//        List<TrainProtoType> trains = new ArrayList<>();
//        TrainProtoType train = TrainProtoType.TrainProtoTypeFactoryMethod("Geetanjali Express", 1212, Station.AHMEDABAD, Station.HYDERABAD);
//        TrainProtoType train1 = TrainProtoType.TrainProtoTypeFactoryMethod("Patanjali supress", 1213, Station.PATNA, Station.CHANDIGARH);
//        TrainProtoType train2= TrainProtoType.TrainProtoTypeFactoryMethod("Snehajali SuperExpress", 1214, Station.MUMBAI, Station.DELHI);
//        TrainProtoType train3 = TrainProtoType.TrainProtoTypeFactoryMethod("Chetavni Express", 1215, Station.CHENNAI, Station.BHOJPUR);
//        List<Station> r1 = new LinkedList<>();
////        List<Day> rd1 = new ArrayList<>();
//        r1.addFirst(Station.BANGALORE);
//        r1.add(Station.HYDERABAD);
//        r1.add(Station.PUNE);
//        r1.add(Station.MUMBAI);
//        r1.add(Station.AHMEDABAD);
//        r1.add(Station.JAIPUR);
//        r1.addLast(Station.DELHI);
//        rd1.add(Day.MONDAY);
//        rd1.add(Day.FRIDAY);
//        train1.setRoute(r1);
//        train1.setRunningDays(rd1);
//        trains.add(train);
//        trains.add(train1);
//        trains.add(train2);
//        trains.add(train3);
////        System.out.println(train.bookSeat(GeneralClassCoach.class, 20));
////        System.out.println(train.bookSeat(GeneralClassCoach.class, 5));
////        System.out.println(train.bookSeat(GeneralClassCoach.class, 30));
////        System.out.println(train.bookSeat(GeneralClassCoach.class, 15));
////        System.out.println(train.bookSeat(GeneralClassCoach.class, 20));
////        System.out.println(train.bookSeat(GeneralClassCoach.class, 5));
////        int x = train.getAvailableSheets(GeneralClassCoach.class);
////        System.out.println(train.getAvailableSheets(FirstClassACCoach.class));
////        System.out.println(train.bookSeat(FirstClassACCoach.class, 1));
////        System.out.println(train.bookSeat(GeneralClassCoach.class, 5));
////        System.out.println(train.getCoaches());
//
//
//        Station src = Station.PUNE ;
//        Station dest = Station.AHMEDABAD;
//        LocalDate today = LocalDate.now().minusDays(2);
//        List<TrainProtoType> availableTrains = new ArrayList<>();
//        for(TrainProtoType t: trains){
//            List<Station> r = t.getRoute();
//            if(r.containsAll(List.of(src, dest)) && r.indexOf(src)<r.indexOf(dest) && t.getRunningDays().stream().map(Day::getDay).toList().contains(today.getDayOfWeek().toString().toUpperCase())){
//                /*I have to complete this program for searching trains by day */
//                availableTrains.add(t);
//            }
//        }
//
//        for(TrainProtoType t: availableTrains){
//            System.out.println(t);
//        }
//        Controller controller = new Controller();
//        boolean programIsRunning = true;
//        System.out.println("Welcome to Train Booking System");
//        do{
//            controller.displayHome();
//        }while(programIsRunning);

//        UserController userController = new UserController();
//        userController.login();
//        int i = userController.bookTicket();
//        userController.viewTickets();

        MainController mc  = new MainController();
        mc.mainHome();
    }
}