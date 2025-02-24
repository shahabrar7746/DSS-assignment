package models;

import intefaces.Formatable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User extends Passenger implements Formatable {
    private boolean isLoggedIn;
    private boolean isActive;
    private String userName;
    private String password;
    private List<Passenger> masterPassengerList = new ArrayList<>();
    private List<Integer> pnrList = new ArrayList<>();

    public User(String Name, int age, String userName, String password) {
        super(Name, age);
        this.userName = userName;
        this.password = password;
        this.isActive = true;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setLoggedIn(boolean isLoogedIn) {
        this.isLoggedIn = isLoogedIn;
    }

    public boolean getIsLoggedIn(){
        return this.isLoggedIn;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void addPassenger(Passenger passenger) {
        this.masterPassengerList.add(passenger);
    }

    public List<Passenger> getMasterPassengerList() {
        return this.masterPassengerList;
    }

    public List<Integer> getPnrList() {
        return pnrList;
    }

    public void setPnrList(List<Integer> pnrList) {
        this.pnrList = pnrList;
    }

    public int getLatestPnr(){
        return this.pnrList.getLast();
    }

    @Override
    public List<String> fieldsToDisplay() {
        return List.of("userName", "isActive", "isLoggedIn");

    }

    @Override
    public List<String> getFieldValues() {
        return List.of(this.userName, String.valueOf(this.isActive), String.valueOf(this.isLoggedIn));
    }


    @Override
    public String getDisplayabletitle() {
        return "Users";
    }
}