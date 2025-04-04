package org.assignment.entities;

import jakarta.persistence.*;
import lombok.*;
import org.assignment.enums.Roles;

import org.assignment.util.ColorCodes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<CartItems> cart;

    @Override
    public String toString() {
        return ColorCodes.BRIGHT_BLUE +
                "+-----------------+-------------------------+-------------------------+--------------------+-------------------------+-----------------+\n" +
                "| Customer ID     | Email                   | Name                    | Address            | Registered On           | Role            |\n" +
                "+-----------------+-------------------------+-------------------------+--------------------+-------------------------+-----------------+\n" +
                String.format("| %-15d | %-23s | %-23s | %-18s | %-23s | %-15s |\n",
                        id, email, name, address, registeredOn, role) +
                "+-----------------+-------------------------+-------------------------+--------------------+-------------------------+-----------------+\n" +
                ColorCodes.RESET;
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
