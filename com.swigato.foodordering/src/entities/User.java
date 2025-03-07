package entities;

import enums.UserRole;
import utility.Formatable;
import validation.UserValidator;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class User implements UserValidator, Formatable {
    private int id;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private boolean isLoggedIn;

    public User(String name, String email, String password, UserRole role) {
       if (!isValidEmail(email) || !isValidPassword(password) || !isValidName(name)) {
        throw new IllegalArgumentException("Invalid user data.");
       }
        this.id = (int) (Math.random() * 10000);
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isLoggedIn = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }



    @Override
    public boolean isValidEmail(String email) {
        return Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matcher(email).matches();
    }

    @Override
    public boolean isValidPassword(String password) {
        if (role == UserRole.ADMIN) {
            return password != null && password.length() >= 8;
        } else {
            return password != null && password.length() >= 6;
        }
    }

    @Override
    public boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() &&
                Pattern.compile("^[A-Za-z\\s]+$").matcher(name).matches();
    }

    @Override
    public String toString() {
        return "User{" + ", id=" + id +
                ", name='" + name + '\'' +
                "email='" + email + '\'' +
                ", role=" + role +
                ", isLoggedIn=" + isLoggedIn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public List<String> fieldsToDisplay() {
        return List.of("id", "Name", "Email", "role", "Logged in");
    }

    @Override
    public List<String> getFieldValues() {
        return List.of(String.valueOf(this.id), this.name, this.email, String.valueOf(this.role), String.valueOf(this.isLoggedIn));
    }

    @Override
    public String getDisplayabletitle() {
        return "USERS";
    }
}