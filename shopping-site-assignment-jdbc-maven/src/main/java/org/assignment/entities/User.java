package org.assignment.entities;

import jakarta.persistence.*;
import lombok.*;
import org.assignment.enums.Roles;

import org.assignment.util.ColorCodes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter

@Setter

@Builder
@NoArgsConstructor
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
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

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<CartItems> cart;

    @Column(name = "logged_in")
    private boolean isLoggedIn;

    @Override
    public String toString() {
        return ColorCodes.BRIGHT_BLUE +
                String.format("| %-15d | %-23s | %-23s | %-18s | %-23s | %-15s |",
                        id, email, name, address, registeredOn, role)
                + ColorCodes.RESET;
    }


    @Override
    public boolean equals(Object obj) {
        User userObj = (User) obj;
        return userObj.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        Integer hash = (int) (this.email.hashCode() + this.id);
        return hash.hashCode();
    }


}
