package org.assignment.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.assignment.enums.Roles;

import org.assignment.util.ColorCodes;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "customer")
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;

    @Column(name = "registered_on")
    private LocalDateTime registeredOn;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Roles role;


    public Customer(String name, String email, String password, String address, LocalDateTime registeredOn, Roles role) {

        this.name = name;
        this.password = password;
        this.email = email;
        this.address = address;
        this.id = id;
        this.registeredOn = registeredOn;
        this.role = role;
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


}
