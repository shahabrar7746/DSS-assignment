package frontend.pages.HomeFunctionality;

import entity.user.User;
import enums.Role;
import server.db.Repository;

import java.util.Scanner;

public abstract class HomeMethods {
    static Scanner sc=new Scanner(System.in);
    //Method to Take user inputs
    public static User userInput(int option) {
        User user;
        System.out.println("************Enter a Detail****************");
        System.out.println("Enter a email : ");
        var email = sc.next();
        System.out.println("Enter a password : ");
        var password = sc.next();
        if (option == 2) {
            System.out.println("Enter a name : ");
            var name = sc.next();
            user = new User(name, email, password, Role.User);
            return user;
        } else {
            user = Repository.userLogin(email, password);
            return user;
        }
    }
}
