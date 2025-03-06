package ui.dashboard;

import model.entity.book.BookBorrowed;
import model.entity.user.User;
import model.entity.enums.BookCategory;
import model.entity.enums.BookCondition;
import server.services.CurdOpertionBorrowBook;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;


public class UserFunctionality {
    static Scanner sc = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(UserFunctionality.class.getName());


    /// userInput for bookBorrow and return
    public static void userBookInput(int caseInput, User user) {
        System.out.println("Enter a BookID :");
        String bookId = sc.next().toUpperCase();
        System.out.println("Enter a Book condition i.e good/damage :");
        String bookCondition = sc.next().toLowerCase();
        if (Arrays.stream(BookCondition.values()).noneMatch(bookConditionStream -> bookConditionStream.toString().equalsIgnoreCase(bookCondition))) {
            throw new InputMismatchException();
        }
        if (caseInput == 3) {
            BookBorrowed bookBorrowed = new BookBorrowed(bookId.toUpperCase(), user.getId(), LocalDate.now(), BookCondition.valueOf(bookCondition.toLowerCase()));
            if (new CurdOpertionBorrowBook(bookBorrowed).add()) {
                logger.info("Book Borrowed Successful..");
            }
        } else if (caseInput == 4) {
            if (new CurdOpertionBorrowBook(user.getId(), bookId, BookCondition.valueOf(bookCondition.toLowerCase())).remove()) {
                logger.info("Return Successful..");
            } else {
                logger.info("invalid data please enter proper data");
            }
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
