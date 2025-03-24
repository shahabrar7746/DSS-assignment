package server.services;

import repository.dao.UserDoa;
import model.entity.user.User;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CurdOperationUser extends Repository {

    //list of user
    public void dispalyUser() {
        new UserDoa().read();
    }

    public boolean updateUsre(User user){
        Scanner sc = new Scanner(System.in);
        UserDoa userDoa = new UserDoa();
        System.out.println("What do u want to update");
        System.out.println("enter 1 for name update : ");
        System.out.println("enter 2 for  email update : ");
        System.out.println("enter 3 for  exit : ");
        int input=0;
        try {
           input = sc.nextInt();
        }catch (InputMismatchException e){
            System.err.println("Enter a proper input..");
            sc.nextLine();
            updateUsre(user);
        }
        switch (input) {
            case 1 -> {
                sc.nextLine();
                System.out.println("Enter a name :");
                String name = sc.nextLine().trim().toLowerCase();
                user.setName(name);
                userDoa.update(user);
                System.out.println("your name update successfully...");
                return true;
            }
            case 2 -> {
                sc.nextLine();
                System.out.println("Enter a old email :");
                String oldEmail = sc.nextLine().trim().toLowerCase();
                System.out.println("Enter a new email :");
                String newEmail = sc.nextLine().trim().toLowerCase();
                if (user.getEmail().equalsIgnoreCase(oldEmail)) {
                    user.setEmail(newEmail);
                    System.out.println("your email update successfully...");
                    return true;
                } else {
                    System.err.println("old email not match..");
                    return false;
                }
            }case 3-> {
                return false;
            }
            default -> {
                System.out.println("Enter a proper input : ");
            }
        }
        return false;
    }

}
