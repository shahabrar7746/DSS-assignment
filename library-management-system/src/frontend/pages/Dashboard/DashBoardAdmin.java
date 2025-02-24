package frontend.pages.Dashboard;

import entity.book.Book;
import entity.user.User;
import frontend.pages.HomeFunctionality.DashBoardMethods;
import frontend.pages.display.DisplayOptions;
import server.db.Repository;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

public class DashBoardAdmin extends DisplayOptions {
    static Scanner sc = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(DashBoardAdmin.class.getName());

    private DashBoardAdmin(){}
    //admin
    public static void dashBoardAdmin(User user) {
        while (true) {
            displayOption("*************Library (Admin)***********************",
                    "Enter 1 for check profile",
                    "Enter 2 for Check Books"
                   ,"Enter 3 for Add Book",
                    "Enter 4 for remove Book",
                    "Enter 5 for transaction Details",
                    "Enter 6 for Display All Users",
                    "Enter 7 for Delete User",
                    "Enter 8 for sign out"
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
                    Repository.printBooks();
                    break;
                }
                case 3: {
                    Book book;
                    try{
                        book = DashBoardMethods.case3Input();
                    }catch (IllegalArgumentException e){
                        System.err.println("Enter a proper input");
                        System.out.println(" ");
                        continue;
                    }
                    Repository.addBookDb(book);
                    logger.info("Book added successfully..");
                    break;
                }
                case 4: {
                    System.out.println("Enter a book name :");
                    String bookName = sc.nextLine();
                    if (Repository.removeBook(bookName)) {
                        System.out.println("Book Remove Successful..");
                    } else {
                        System.out.println("Book Not found..");
                    }
                    break;
                }
                case 5: {
                    Repository.getBorrowedData();
                    break;
                } case 6:{
                    Repository.diplayAllUser();
                    break;
                }
                case 7:{
                    System.out.println("Enter email of user want to delete");
                    String email = sc.next();
                    if(Repository.deleteUser(email)){
                        logger.info("User delete Successfully..");
                    }
                    break;
                }
                case 8: {
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
