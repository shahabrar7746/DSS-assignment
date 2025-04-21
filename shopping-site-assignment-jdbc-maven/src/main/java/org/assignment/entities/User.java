package org.assignment.entities;

import jakarta.persistence.*;
import lombok.*;
import org.assignment.enums.Roles;

import org.assignment.util.ColorCodes;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false, unique = true)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;


    @CreationTimestamp
    @Column(name = "registered_on", nullable = false, updatable = false)
    private LocalDateTime registeredOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Roles role;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<CartItems> cart;

    @Column(name = "logged_in", nullable = false)
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
