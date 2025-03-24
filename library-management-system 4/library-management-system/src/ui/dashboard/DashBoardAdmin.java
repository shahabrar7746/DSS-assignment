package ui.dashboard;

import customexception.DataReadException;
import entity.Book;
import entity.User;
import enums.BookCategory;
import enums.ResponseStatus;
import repository.dao.BookDao;
import repository.dao.UserDao;
import repository.daoimpl.AdminDao;
import repository.daoimpl.BookDaoImpl;
import repository.daoimpl.UserDaoImpl;
import servicesImpl.BookServicesImpl;
import servicesImpl.UserServicesimpl;
import servicesImpl.BorrowedBookServicesImpl;
import ui.AbstractUi;
import utils.Response;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import static ui.dashboard.UserFunctionality.dispalyCategoryEnumValue;

public class DashBoardAdmin extends AbstractUi {
    static Scanner sc = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(DashBoardAdmin.class.getName());
    BookDao bookDao = BookDaoImpl.getBookInstance();
    UserDao userDao = new UserDaoImpl();
    UserServicesimpl userServicesimpl=new UserServicesimpl(userDao);



    //admin
   public void AdminScreen(User user) {

       while (true) {
            displayOption(List.of("*************Library (Admin)***********************",
                    "Enter 1 for check profile",
                    "Enter 2 for Check Books",
                    "Enter 3 for Add Book",
                    "Enter 4 for remove Book",
                    "Enter 5 for transaction Details",
                    "Enter 6 for Display All Users",
                //  "Enter 7 for role update Users",
                     "Enter 8 for sign out")
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
                     BookServicesImpl.getBookInstance().DisplayBook();
                    break;
                }
                case 3: {
                    bookAdd();
                    break;
                }
                case 4: {
                   removeBook();
                    break;
                }
                case 5: {
                    //trascation
                    break;
                }
                case 6: {
                    dispalyUser();
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

    public void dispalyUser() {
        try {
            userServicesimpl.getUser();
        } catch (Exception e) {
        //   log.error("something went wrong..");
            System.out.println("something went wrong..");
        }
    }

    //Add Book
    private void bookAdd() {
        try {
            System.out.println("Enter a Book name : ");
            String bookName = sc.nextLine();
            System.out.println("Enter a Book Author : ");
            String bookAuthor = sc.nextLine();
            System.out.println("Enter a Book Category : ");
            dispalyCategoryEnumValue();
            String category = sc.next();
            if (Arrays.stream(BookCategory.values()).noneMatch(enums -> enums.toString().equalsIgnoreCase(category))) {
                sc.nextLine();
                System.out.println(" ");
                System.err.println("Enter a proper category...");
            }
            System.out.println("Enter a Book Copy : ");
            int copy = sc.nextInt();
            Book book = new Book(bookName, bookAuthor, BookCategory.valueOf(category.toUpperCase().trim()), copy);
            Response bookResponse =  BookServicesImpl.getBookInstance().addBook(book);
            if (bookResponse.getStatusCode().toString().equalsIgnoreCase(ResponseStatus.SUCCESS.toString())) {
                System.out.println(bookResponse.getMessage());
            } else {
                System.out.println("something went wrong try again");
            }
        } catch (InputMismatchException | IllegalArgumentException e) {
            System.err.println("Enter a proper value :");
        }
    }

    public void updateRole() {
        try {
            System.out.println("Enter a user email: ");
            String userEmail = sc.nextLine();
            System.out.println("Enter a role : ");
            String role = sc.nextLine().toLowerCase();
           // new AdminDao().updateRole(userEmail,role);
            System.out.println(" ");
            System.out.println("user Update Successful..");
            System.out.println(" ");
        } catch (InputMismatchException  e) {
            System.out.println("Enter a proper input : ");
        }
    }

    //delete book
    private void removeBook() {
        System.out.println("Enter a book srNo to delete :");
        String srNo = sc.nextLine();
        System.out.println("Enter a how many copy want to delete :");
        int noCopy = sc.nextInt();
        if ( BookServicesImpl.getBookInstance().bookRemove(srNo,noCopy)) {
            System.out.println("Book Remove Successful..");
        } else {
            System.out.println("Book Not found..");
        }
    }
}


