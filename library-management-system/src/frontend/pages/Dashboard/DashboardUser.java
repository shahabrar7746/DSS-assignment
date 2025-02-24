package frontend.pages.Dashboard;

import entity.user.User;
import frontend.pages.HomeFunctionality.DashBoardMethods;
import frontend.pages.display.DisplayOptions;
import server.db.Repository;

import java.util.InputMismatchException;
import java.util.Scanner;



public class DashboardUser extends DisplayOptions {
    static Scanner sc = new Scanner(System.in);
    private DashboardUser(){
    }

    public static void dashBoardUser(User user) {
        while (true) {
            displayOption("*************Library***********************",
                    "Enter 1 for profile",
                    "Enter 2 for Check Books",
                    "Enter 3 for Borrow Book",
                    "Enter 4 for Return Book",
                    "Enter 5 for sign out");
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
                    Repository.printBooks();
                    break;
                }
                case 3: {
                    try{
                        DashBoardMethods.userBookInput(3,user);
                    }catch (InputMismatchException e){
                        System.err.println("enter a proper value");
                        sc.nextLine();
                    }
                    break;
                }
                case 4: {
                    try {
                        DashBoardMethods.userBookInput(4,user);
                    }catch (InputMismatchException e){
                        System.err.println("enter a proper value");
                        sc.nextLine();
                    }
                    break;
                }
                case 5: {
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
