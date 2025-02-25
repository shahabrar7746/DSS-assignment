package entities;

import enums.Roles;

import util.ColorCodes;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Customer {
    private String email;
    private String password;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    private String name;
    private String address;
    private Long id;

    private Timestamp registeredOn;
    private Roles role;

    public Date getRegisteredOn() {
        return registeredOn;
    }

    public Roles getRole() {
        return role;
    }

    public Customer(Long id, String name, String email, String password, String address, Timestamp registeredOn, Roles role) {
        this(name, email, password, address);
        this.id = id;
        this.registeredOn = registeredOn;
        this.role = role;
    }
    //name, email, password, address
    public Customer(String name,String email, String password, String address ){
        this.id = new Random().nextLong(0, 8000); //randomly chose between 0 - 8000.
        this.name = name;
        this.password = password;
        this.email = email;
        this.address = address;


        this.registeredOn = new Timestamp(System.currentTimeMillis());
        this.role = Roles.CUSTOMER;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }



//    public void addOrder(Order order) {
//        if (orderList == null) {
//            orderList = new ArrayList<>();
//        }
//        this.orderList.add(order);
//    }
//
//    public void cancelOrder(Order order) {
//        if (orderList == null) {
//            return;
//        }
//        this.orderList.remove(order);
//    }



    @Override
    public String toString() {
        return ColorCodes.BRIGHT_BLUE + "Customer{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", id=" + id +
                ", registeredOn=" + registeredOn +
                ", role=" + role +
                "}\n" + ColorCodes.RESET;
    }

    @Override
    public boolean equals(Object obj) {
        Customer customerObj = (Customer) obj;
        return customerObj.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        Integer hash = (int) (this.email.hashCode() + this.id);
        return hash.hashCode();
    }

    public Long getId() {
        return id;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
