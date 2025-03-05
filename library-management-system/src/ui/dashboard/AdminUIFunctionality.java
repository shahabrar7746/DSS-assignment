package ui.dashboard;

import repository.dao.BookDao;
import model.entity.book.Book;
import model.entity.enums.BookCategory;
import server.services.AdminOperation;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

import static ui.dashboard.UserFunctionality.dispalyCategoryEnumValue;

public class AdminUIFunctionality {

    static Scanner sc = new Scanner(System.in);

    //add book
    public void bookAdd() throws IllegalArgumentException {
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
            System.out.println("Enter a Book srno : ");
            long srNo = sc.nextLong();
            System.out.println("Enter a Book Copy : ");
            int copy = sc.nextInt();
            Book book = new Book(bookName, bookAuthor, BookCategory.valueOf(category.toUpperCase().trim()), srNo, copy);
            if (new AdminOperation(book).bookInsert()) {
                System.out.println("Book added successfully..");
            } else {
                System.out.println("something went wrong try again");
            }
        } catch (InputMismatchException | IllegalArgumentException e) {
            System.err.println("Enter a proper value :");
        }
    }


    //delete book
    public void removeBook() {
        System.out.println("Enter a book name :");
        String bookName = sc.nextLine();
        if (new BookDao().delete(bookName)) {
            System.out.println("Book Remove Successful..");
        } else {
            System.out.println("Book Not found..");
        }
    }

    //update book

    public void updateBook(){

    }

    //display book
    public void displayBook(){
        new BookDao().read();
    }

}