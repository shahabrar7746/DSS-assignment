package org.assignment.entities;

import jakarta.persistence.*;
import lombok.*;
import org.assignment.enums.Roles;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "seller")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Seller implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;


    @Column(name = "name")
    private String name;

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Seller seller = (Seller) object;
        return Objects.equals(id, seller.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Roles role;
    @Column(name = "registered_on")
private LocalDateTime registeredOn;


    @Override
    public String toString() {
        StringBuilder table = new StringBuilder();
        table.append("+------------+-----------------+------------+\n");
        table.append("| ID         | Name            | Role       |\n");
        table.append("+------------+-----------------+------------+\n");
        table.append(String.format("| %-10s | %-15s | %-10s |\n", "ID: " + id, "Name: " + name, "Role: " + role));
        table.append("+------------+-----------------+------------+\n");
        return table.toString();
    }

}
