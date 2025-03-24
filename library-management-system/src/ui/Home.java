package ui;

import repository.dao.AuthDoa;
import model.entity.user.User;
import ui.dashboard.DashBoardAdmin;
import ui.dashboard.DashboardUser;
import ui.homefunctionality.HomeMethods;
import ui.display.DisplayOptions;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

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
                        if (user.getRole().toString().equalsIgnoreCase("user")) {
                          new DashboardUser().UserScreen(user);
                        } else {
                            new DashBoardAdmin().AdminScreen(user);
                        }
                    } else {
                        System.out.println("Invalid User");
                    }
                    break;
                }
                case 2: {
                    User user = HomeMethods.userInput(choice);
                    new AuthDoa(user).userRegistration();
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
