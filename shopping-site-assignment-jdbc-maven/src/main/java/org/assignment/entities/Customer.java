package org.assignment.entities;

import org.assignment.enums.Roles;

import org.assignment.util.ColorCodes;

import java.time.LocalDateTime;

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

    private LocalDateTime registeredOn;
    private Roles role;

    public LocalDateTime getRegisteredOn() {
        return registeredOn;
    }

    public Roles getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer(String name, String email, String password, String address, LocalDateTime registeredOn, Roles role) {

        this.name = name;
        this.password = password;
        this.email = email;
        this.address = address;
        this.id = id;
        this.registeredOn = registeredOn;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

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
