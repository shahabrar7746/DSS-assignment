package ui.dashboard;

import model.entity.user.User;
import server.services.CurdOperationUser;
import server.services.CurdOperationsBook;

import java.util.InputMismatchException;
import java.util.Scanner;

public class DashboardUser extends Ui {
    static Scanner sc = new Scanner(System.in);


   public void UserScreen(User user) {
        while (true) {
            displayOption("*************Library***********************",
                    "Enter 1 for profile",
                    "Enter 2 for Check Books",
                    "Enter 3 for Borrow Book",
                    "Enter 4 for Return Book",
                    "Enter 5 for  Update User",
                    "Enter 6 for sign out");
            int choice;
            try {
                displayOption("Enter Your Options:");
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("please enter proper input");
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1: {
                    System.out.println(user);
                    break;
                }
                case 2: {
                    new CurdOperationsBook().read();
                    break;
                }
                case 3: {
                    try{
                        UserFunctionality.userBookInput(3,user);
                    }catch (InputMismatchException e){
                        System.err.println("enter a proper value");
                        sc.nextLine();
                    }
                    break;
                }
                case 4: {
                    try {
                        UserFunctionality.userBookInput(4,user);
                    }catch (InputMismatchException e){
                        System.err.println("enter a proper value");
                        sc.nextLine();
                    }
                    break;
                }
                case 5: {
                    new CurdOperationUser().updateUsre(user);
                    break;
                }
                case 6: {
                    user.setLogin(false);
                    return;
                }
                default: {
                    System.out.println("wrong input");
                    break;
                }

            }
        }
    }
}
