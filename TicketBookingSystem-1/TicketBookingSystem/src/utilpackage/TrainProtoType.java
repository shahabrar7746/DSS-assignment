package utilpackage;

import enums.Day;
import enums.Station;
import intefaces.Formatable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainProtoType implements Formatable {
    private int trainNumber;
    private String trainName;
    private Station source;
    private Station destination;
    private List<Coach> coaches = new ArrayList<>();
    private List<Day> runningDays = new ArrayList<>();
    private List<Station> route = new ArrayList<>();
    Map<Integer, Class<?>> AvailableCoacheClasses = new HashMap<>();

    private TrainProtoType(String trainName, int trainNumber) {
        this.trainName = trainName;
        this.trainNumber = trainNumber;
        this.attachCoaches();
        this.setAvailableCoacheClasses();
    }

    private void attachCoaches() {
        this.coaches.add(new FirstClassACCoach("A1", 3700));
        this.coaches.add(new FirstClassACCoach("A2", 3700));
        this.coaches.add(new SecondClassACCoach("B1", 2700));
        this.coaches.add(new SecondClassACCoach("B2", 2700));
        this.coaches.add(new ThirdClassAcCoach("C1", 1700));
        this.coaches.add(new ThirdClassAcCoach("C2", 1700));
        this.coaches.add(new SleeperClassCoach("SL1", 700));
        this.coaches.add(new SleeperClassCoach("SL2", 700));
        this.coaches.add(new SleeperClassCoach("SL3", 700));
        this.coaches.add(new GeneralClassCoach("GEN", 300));
    }

    private void setAvailableCoacheClasses(){
        int count = 0;
        for(Coach coach: coaches){
            if(! this.AvailableCoacheClasses.containsValue(coach.getClass())){
                AvailableCoacheClasses.put(count++, coach.getClass());
            }
        }
    }

    public Map<Integer, Class<?>> getAvailableCoacheClasses() {
        return this.AvailableCoacheClasses;
    }

    public static TrainProtoType trainProtoTypeFactoryMethod(String trainName, int trainNumber) {
        return new TrainProtoType(trainName, trainNumber);
    }

    public int getTrainNumber() {
        return this.trainNumber;
    }

    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    private List<Coach> getCoaches() {
        return this.coaches;
    }

    private void setCoaches(List<Coach> coaches) {
        this.coaches = coaches;
    }

    public int getAvailableSeats(Class<?> coachClass) {
        int availableSeats = 0;
        for (Coach coach : coaches) {
            if (coach.getClass().getName().equals(coachClass.getName())) {
                availableSeats += coach.getAvailableSeats();
            }
        }
        return availableSeats;
    }

    public String bookSeat(Class<?> coachClass) {
        for (Coach coach : coaches) {
            if (coach.getClass().getName().equals(coachClass.getName())) {
                if (coach.getAvailableSeats() >= 1) {
                    int temp = coach.getTotal_seats() - coach.getAvailableSeats() + 1;
                    coach.bookSeat(1);
                    return coach.getCoachNumber() + "-" + (temp);
                }
            }
        }
        return null;
    }

    public void setDestination(Station dest) {
        this.destination = dest;
    }

    public void setSource(Station src) {
        this.source = src;
    }

    public Station getSrc() {
        return this.source;
    }

    public Station getDest() {
        return this.destination;
    }

    public List<Station> getRoute() {
        return this.route;
    }

    public void setRoute(List<Station> route) {
        this.route = route;
        this.source = route.get(0);
        this.destination = route.get(route.size()-1);
    }

    @Override
    public String toString() {
        return this.trainName;
    }

    public List<Day> getRunningDays() {
        return this.runningDays;
    }

    public void setRunningDays(List<Day> runningDays) {
        this.runningDays = runningDays;
    }

    @Override
    public List<String> fieldsToDisplay() {
        return List.of("trainNumber", "trainName", "source", "destination");
    }

    @Override
    public List<String> getFieldValues() {
        return List.of(String.valueOf(this.trainNumber), this.trainName, this.source.getStationName(), this.destination.getStationName());
    }

    @Override
    public String getDisplayabletitle() {
        return "Trains";
    }


    private class Coach {
        private String coachNumber;
        private double fair;
        private int total_seats = 80;
        private int availableSeats = 80;

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

        public boolean bookSeat(int quantity) {
            if (quantity <= this.availableSeats) {
                this.availableSeats = this.availableSeats - quantity;
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return this.getCoachNumber();
        }
    }

    private class FirstClassACCoach extends Coach {

        public FirstClassACCoach(String coachNumber, double fair) {
            super(coachNumber, fair);
        }
    }

    private class SecondClassACCoach extends Coach {
        public SecondClassACCoach(String coachNumber, double fair) {
            super(coachNumber, fair);
        }
    }

    private class ThirdClassAcCoach extends Coach {
        public ThirdClassAcCoach(String coachNumber, double fair) {
            super(coachNumber, fair);
        }
    }

    private class SleeperClassCoach extends Coach {
        public SleeperClassCoach(String coachNumber, double fair) {
            super(coachNumber, fair);
        }
    }

    private class SecondSittingClassCoach extends Coach {
        public SecondSittingClassCoach(String coachNumber, double fair) {
            super(coachNumber, fair);
        }
    }

    private class GeneralClassCoach extends Coach {
        public GeneralClassCoach(String coachNumber, double fair) {
            super(coachNumber, fair);
        }
    }


}
