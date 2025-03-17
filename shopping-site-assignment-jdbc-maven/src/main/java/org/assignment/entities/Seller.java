package org.assignment.entities;

import org.assignment.enums.Roles;

import java.time.LocalDateTime;

public class Seller {
    private Long id;
    private String name;
    private Roles role;
private LocalDateTime registeredOn;
    public Roles getRole() {
        return role;
    }

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


    public Seller(Long id, String name, Roles role, LocalDateTime registeredOn){
        this.id = id;
        this.name = name;
        this.role = role;
        this.registeredOn = registeredOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }
}
