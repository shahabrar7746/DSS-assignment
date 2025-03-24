package ui.dashboard;

import entity.BookBorrowed;
import entity.User;
import enums.BookCondition;
import enums.ResponseStatus;
import repository.dao.BorrowDao;
import repository.dao.UserDao;
import repository.daoimpl.BookDaoImpl;
import repository.daoimpl.BorrowDoaImpl;
import repository.daoimpl.UserDaoImpl;
import server.BorrowedBookServices;
import servicesImpl.BookServicesImpl;
import servicesImpl.BorrowedBookServicesImpl;
import servicesImpl.UserServicesimpl;
import ui.AbstractUi;
import utils.Response;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DashboardUser extends AbstractUi {
    static Scanner sc = new Scanner(System.in);
    BookDaoImpl bookDao = BookDaoImpl.getBookInstance();
    UserDao userDao=new UserDaoImpl();
    BorrowDao borrowDao=new BorrowDoaImpl(bookDao,userDao);


   public void UserScreen(User user) {

       while (true) {
            displayOption(List.of("*************Library***********************",
                    "Enter 1 for profile",
                    "Enter 2 for Check Books",
                    "Enter 3 for Borrow Book",
                    "Enter 4 for Return Book",
                    "Enter 5 for  Update User",
                    "Enter 6 for sign out"));
            int choice;
            try {
                System.out.println("Enter Your Options:");
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
                     BookServicesImpl.getBookInstance().DisplayBook();
                    break;
                }
                case 3: {
                    try{
                     userBookInput(3,user);
                    }catch (InputMismatchException e){
                        System.err.println("enter a proper value");
                        sc.nextLine();
                    }
                    break;
                }
                case 4: {
                    try {
                        userBookInput(4,user);
                    }catch (InputMismatchException e){
                        System.err.println("enter a proper value");
                        sc.nextLine();
                    }
                    break;
                }
                case 5: {
                    new UserServicesimpl(userDao).updateUsre(user);
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



    private  void userBookInput(int caseInput, User user) {
        System.out.println("Enter a BookID :");
        String bookId = sc.next().toUpperCase();
        BorrowedBookServices bookServices =new BorrowedBookServicesImpl(borrowDao);
        if (caseInput == 3) {
            BookBorrowed bookBorrowed = new BookBorrowed(bookId.toUpperCase(), user.getId(), LocalDate.now());
            Response response=bookServices.borrowBook(bookBorrowed);
            if(response.getStatusCode().toString().equalsIgnoreCase(ResponseStatus.SUCCESS.toString())){
                System.out.println(response.getMessage());
            }else {
                System.out.println(response.getMessage());
            }
        } else if (caseInput == 4) {
            Response response=bookServices.returnBook(bookId,user);
            if(response.getStatusCode().toString().equalsIgnoreCase(ResponseStatus.SUCCESS.toString())){
                System.out.println(response.getMessage());
            }else {
                System.out.println(response.getMessage());
            }
        }
    }
}
