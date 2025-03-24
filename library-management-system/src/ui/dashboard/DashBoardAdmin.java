package ui.dashboard;

import model.entity.user.User;
import server.services.CurdOperationUser;
import server.services.CurdOperationsBook;
import server.services.CurdOpertionBorrowBook;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

public class DashBoardAdmin extends Ui {
    static Scanner sc = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(DashBoardAdmin.class.getName());

    //admin
   public void AdminScreen(User user) {
        while (true) {
            displayOption("*************Library (Admin)***********************",
                    "Enter 1 for check profile",
                    "Enter 2 for Check Books"
                    , "Enter 3 for Add Book",
                    "Enter 4 for remove Book",
                    "Enter 5 for transaction Details",
                    "Enter 6 for Display All Users",
                    "Enter 7 for sign out"
            );

            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                logger.info("please enter proper input");
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
                    new AdminUIFunctionality().bookAdd();
                    break;
                }
                case 4: {
                    new AdminUIFunctionality().removeBook();
                    break;
                }
                case 5: {
                    new CurdOpertionBorrowBook().read();
                    break;
                }
                case 6: {
                    new CurdOperationUser().dispalyUser();
                    break;
                }
                case 7: {
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


