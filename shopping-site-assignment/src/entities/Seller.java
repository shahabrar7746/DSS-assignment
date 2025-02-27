package entities;

import enums.Roles;
import util.ColorCodes;

import java.sql.Timestamp;
import java.util.Random;

public class Seller {
    private Long id;
    private String name;
    private Roles role;
private Timestamp timestamp;
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

    public Seller(String name) {
        this.name = name;
        this.id = new Random().nextLong(0, 700);
        this.role = Roles.SELLER;
    }
    public Seller(Long id, String name, Roles role, Timestamp timestamp){
        this(name);
        this.id = id;
        this.name = name;
        this.role = role;
        this.timestamp = timestamp;
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
