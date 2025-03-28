package com.foodorder.app.entities;

import com.foodorder.app.enums.UserRole;
import com.foodorder.app.utility.Formatable;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Builder
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class User implements Formatable {
    private Long userId;
    private String name;
    private String email;
    private String password;
    private String address;
    private UserRole role;
    private boolean isLoggedIn;

//    public boolean isLoggedIn() {
//        return isLoggedIn;
//    }
//
//    public void setLoggedIn(boolean loggedIn) {
//        isLoggedIn = loggedIn;
//    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email);
    }

    @Override
    public List<String> fieldsToDisplay() {
        return List.of("id", "Name", "Email", "role", "Logged in");
    }

    @Override
    public List<String> getFieldValues() {
        return List.of(String.valueOf(this.userId), this.name, this.email, String.valueOf(this.role), String.valueOf(this.isLoggedIn));
    }

    @Override
    public String getDisplayabletitle() {
        return "USERS";
    }
}