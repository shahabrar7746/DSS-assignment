package frontend.pages;

import entity.user.User;
import frontend.pages.Dashboard.DashBoardAdmin;
import frontend.pages.Dashboard.DashboardUser;
import frontend.pages.HomeFunctionality.HomeMethods;
import frontend.pages.display.DisplayOptions;
import server.db.Repository;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

public class Home extends DisplayOptions {
    static Scanner sc = new Scanner(System.in);

    private Home() {
    }

    static {
        System.out.println("*********************************Welcome****************************************************");
    }

    public static void homeScreen() throws IOException {
        boolean isExit = true;
        int choice = 0;
        while (isExit) {
            displayOption("*********************************Login**************************************************",
                    "Enter 1 for login :",
                    "Enter 2 for SignUp:","Enter 3 for Exit:");
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("please enter proper input");
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1: {
                    User user = HomeMethods.userInput(choice);
                    if (user != null) {
                        if (!user.getEmail().equalsIgnoreCase("admin@gmail.com")) {
                            DashboardUser.dashBoardUser(user);
                        } else {
                            DashBoardAdmin.dashBoardAdmin(user);
                        }
                    } else {
                        System.out.println("Invalid User");
                    }
                    break;
                }
                case 2: {
                    User user = HomeMethods.userInput(choice);
                    Repository.userRegistration(user);
                    break;
                }
                case 3: {
                    isExit = false;
                    break;
                }
                default: {
                    System.out.println("Invalid operation : ");
                }
            }
        }
    }
}
