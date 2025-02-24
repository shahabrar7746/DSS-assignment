package entities;

import enums.Roles;

import util.ColorCodes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Customer {
    private String email;
    private String password;
    private String name;
    private String address;
    private Long id;
    private List<Order> orderList;
    private Date registeredOn;
    private Roles role;

    public Date getRegisteredOn() {
        return registeredOn;
    }

    public Roles getRole() {
        return role;
    }

    public Customer(String name, String email, String password, String address) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.address = address;
        this.orderList = new ArrayList<>();
        this.id = new Random().nextLong(0, 8000); //randomly chose between 0 - 8000.
        this.registeredOn = new Date();
        this.role = Roles.CUSTOMER;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    public Long getId() {
        return id;
    }

    public List<Order> getOrders() {
        return orderList;
    }

    public void addOrder(Order order) {
        if (orderList == null) {
            orderList = new ArrayList<>();
        }
        this.orderList.add(order);
    }

    public void cancelOrder(Order order) {
        if (orderList == null) {
            return;
        }
        this.orderList.remove(order);
    }

    @Override
    public String toString() {
        return ColorCodes.BRIGHT_BLUE + "Customer{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", id=" + id +
                ", orderList=" + orderList +
                ", registeredOn=" + registeredOn +
                ", role=" + role +
                "}\n" + ColorCodes.RESET;
    }

    @Override
    public boolean equals(Object obj) {
        Customer customerObj = (Customer) obj;
        return customerObj.email.equals(this.email);
    }

    @Override
    public int hashCode() {
        Integer hash = (int) (this.email.hashCode() + this.id);
        return hash.hashCode();
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
