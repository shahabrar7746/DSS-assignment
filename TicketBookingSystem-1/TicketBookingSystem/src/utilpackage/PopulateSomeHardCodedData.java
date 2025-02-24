package utilpackage;

import enums.Day;
import enums.Station;
import models.User;

import java.util.*;

public class PopulateSomeHardCodedData {
    public static Map<Integer, TrainProtoType> populateTrain(List<List<Station>> routes){
        Map<Integer, TrainProtoType> trains = new HashMap<>();
        if (routes.isEmpty()){
            System.out.println("Please populate routes before populating trains");
            return null;
        }
        TrainProtoType train = TrainProtoType.trainProtoTypeFactoryMethod("Geetanjali Express", 1212);
        TrainProtoType train1 = TrainProtoType.trainProtoTypeFactoryMethod("Patanjali supress", 1213);
        TrainProtoType train2= TrainProtoType.trainProtoTypeFactoryMethod("Snehajali SuperExpress", 1214);
        TrainProtoType train3 = TrainProtoType.trainProtoTypeFactoryMethod("Chetavni Express", 1215);
        TrainProtoType train4 = TrainProtoType.trainProtoTypeFactoryMethod("Vande Bharat", 1215);
        train.setRoute(routes.get(0));
        train1.setRoute(routes.get(1));
        train2.setRoute(routes.get(2));
        train3.setRoute(routes.get(3));
        train4.setRoute(routes.get(4));
        trains.put(train.getTrainNumber(),train);
        trains.put(train1.getTrainNumber(), train1);
        trains.put(train2.getTrainNumber(), train2);
        trains.put( train3.getTrainNumber(), train3);
        trains.put(train4.getTrainNumber(), train4);
        return trains;
    }

    public static Map<String, User> populateUsers(){
        Map<String, User> users = new HashMap<>();
        User dholu = new User("Dholu Kumar",18,"d", "d");
        User bholu = new User("Bholu Kumar",19,"Bholu", "bholu@321");
        User kalia = new User("Kalia Kumar",20,"Kalia", "kalia@321");
        users.put(dholu.getUserName(), dholu);
        users.put(bholu.getUserName(), bholu);
        users.put(kalia.getUserName(), kalia);
        return users;
    }

    public static List<List<Station>> populateRoutes(){
        List<List<Station>> routes = new ArrayList<>();
        List<Station> route1 = new ArrayList<>();
        List<Station> route2 = new ArrayList<>();
        List<Station> route3 = new ArrayList<>();
        List<Station> route4 = new ArrayList<>();
        List<Station> route5 = new ArrayList<>();
        route1.add(Station.DELHI);
        route1.add(Station.JAIPUR);
        route1.add(Station.AHMEDABAD);
        route1.add(Station.MUMBAI);
        route1.add(Station.PUNE);

        route2.add(Station.BANGALORE);
        route2.add(Station.HYDERABAD);
        route2.add(Station.CHENNAI);
        route2.add(Station.KOCHI);
        route2.add(Station.VARANASI);

        route3.add(Station.KOLKATA);
        route3.add(Station.PATNA);
        route3.add(Station.RANCHI);
        route3.add(Station.BHUBANESWAR);
        route3.add(Station.KANPUR);

        route4.add(Station.LUCKNOW);
        route4.add(Station.KANPUR);
        route4.add(Station.INDORE);
        route4.add(Station.SURAT);
        route4.add(Station.AHMEDABAD);

        route5.add(Station.MUMBAI);
        route5.add(Station.PUNE);
        route5.add(Station.BANGALORE);
        route5.add(Station.CHENNAI);
        route5.add(Station.HYDERABAD);

        routes.add(route1);
        routes.add(route2);
        routes.add(route3);
        routes.add(route4);
        routes.add(route5);

        return routes;
    }
}
