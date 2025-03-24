package repository.daoimpl;

import enums.Role;
import entity.User;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class AdminDao {

    Scanner sc = new Scanner(System.in);

//    public void updateRole(String userEmail , String role) {
//        try {
//            User user = DataBaseImpl.usersDb.get(userEmail);
//            if (Objects.nonNull(user)) {
//                Role role1 = user.getRole();
//                if (role1.toString().equalsIgnoreCase(role)) {
//
//                    System.err.println("User already a " + role);
//                } else {
//                    user.setRole(Role.valueOf(role));
//                }
//            } else {
//                System.out.println("User not found");
//            }
//        } catch (InputMismatchException | IllegalArgumentException e) {
//            System.out.println("enter proper input");
//        }
//    }
}
