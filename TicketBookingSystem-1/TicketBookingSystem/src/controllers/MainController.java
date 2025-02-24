package controllers;

import utilpackage.UtilClass;

import java.util.Scanner;

public class MainController {
    UserController userController = new UserController();
    private static Scanner input = UtilClass.scanner;
    public void userOptions(){
        boolean flag = true;
        while(flag){
            System.out.println("User Options:\n[1] Search Trains\n[2] Book Ticket\n[3] View Tickets\n[4] Cancel Ticket\n[5] Logout\n[0] Exit");
            System.out.print("user choice: ");
            int userChoice = input.nextInt();
            if (input.hasNextLine()){
                input.nextLine();
            }
            switch (userChoice){
                case 1:
                    userController.searchTrains();
                    break;
                case 2:
                    userController.bookTicket();
                    break;
                case 3:
                    userController.viewTickets();
                    break;
                case 4:
                    userController.cancelTicket();
                    break;
                case 5:
                    userController.logout();
                    break;
                case 0:
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid Option.");
            }
        }
    }

    public void mainHome(){
        boolean flag = true;
        while(flag) {
            System.out.println("User Options:\n[1] Search Trains\n[2] Login as admin\n[3] login as user\n[4] Register new user\n[0] Exit");
            System.out.print("user choice: ");
            int userChoice = input.nextInt();
            if (input.hasNextLine()) {
                input.nextLine();
            }
            switch (userChoice) {
                case 1:
                    userController.searchTrains();
                    break;
                case 2:
                    break;
                case 3:
                    int login = userController.login();
                    if (login == 1) {
                        userOptions();
                    }

                    break;
                case 4:
                    userController.cancelTicket();
                    break;
                case 5:
                    break;
                case 0:
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid Option.");
            }
        }}}
