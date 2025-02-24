package frontend.pages.HomeFunctionality;

import entity.book.Book;
import entity.book.BookBorrowed;
import entity.user.User;
import enums.BookCategory;
import enums.BookCondition;
import frontend.pages.Dashboard.DashBoardAdmin;
import frontend.pages.Dashboard.DashboardUser;
import server.db.Repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;



public class DashBoardMethods {
    static Scanner sc=new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(DashBoardMethods.class.getName());



    /// userInput for bookBorrow and return
    public static void userBookInput(int caseInput, User user) {
        System.out.println("Enter a BookID :");
        String bookId = sc.next().toUpperCase();
        System.out.println("Enter a Book condition i.e good/damage :");
        String bookCondition = sc.next().toLowerCase();
        if(Arrays.stream(BookCondition.values()).noneMatch(bookConditionStream->bookConditionStream.toString().equalsIgnoreCase(bookCondition))){
            throw new InputMismatchException();
        }
        if (caseInput == 3) {
            BookBorrowed bookBorrowed = new BookBorrowed( bookId.toUpperCase(), user.getId(), LocalDate.now(), BookCondition.valueOf(bookCondition.toLowerCase()));
            if (Repository.printBorrowBook(bookBorrowed)) {
                logger.info("Book Borrowed Successful..");
            }
        } else if (caseInput == 4) {
            if (Repository.returnBook(user.getId(), bookId, BookCondition.valueOf(bookCondition.toLowerCase()))) {
                logger.info("Return Successful..");
            }else {
                logger.info("invalid data please enter proper data");
            }
        }
    }


    /// input for switch case 3
    public static Book case3Input() throws IllegalArgumentException{
        try {
            System.out.println("Enter a Book name : ");
            String bookName = sc.nextLine();
            System.out.println("Enter a Book Author : ");
            String bookAuthor = sc.nextLine();
            System.out.println("Enter a Book Price : ");
            double bookPrice = sc.nextDouble();
            System.out.println("Enter a Book Category : ");
            dispalyCategoryEnumValue();
            String category = sc.next();
            if (Arrays.stream(BookCategory.values()).noneMatch(enums -> enums.toString().equalsIgnoreCase(category))) {
                throw new IllegalArgumentException();
            }
            System.out.println("Enter a Book srno : ");
            long srNo = sc.nextLong();
            System.out.println("Enter a Book Copy : ");
            int copy = sc.nextInt();
            System.out.println(BookCategory.valueOf(category.toUpperCase()));
            return new Book(bookName, bookAuthor, BookCategory.valueOf(category.toUpperCase().trim()), srNo, copy);
        } catch (InputMismatchException e) {
            throw new IllegalArgumentException();
        }
    }

    public static void dispalyCategoryEnumValue() {
        System.out.print("Categories :[");
        for (BookCategory e : BookCategory.values()) {
            System.out.print(e + ",");
        }
        System.out.println("]");
    }


}
