package server.services;

import repository.dao.BookDao;
import model.entity.book.Book;
import model.entity.enums.Role;
import model.entity.user.User;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class AdminOperation {
    private Book book;
    private String bookName;

    public AdminOperation(Book book) {
        this.book = book;
    }

    public AdminOperation(String bookName) {
        this.bookName = bookName;
    }

    static Scanner sc = new Scanner(System.in);

    public void removeBook() {
        System.out.println("Enter a book name :");
        String bookName = sc.nextLine();
        if (new BookDao().delete(bookName)) {
            System.out.println("Book Remove Successful..");
        } else {
            System.out.println("Book Not found..");
        }
    }

    //bookOperation
    public boolean bookInsert() {
        try {
            return new BookDao().add(book);
        } catch (RuntimeException e) {
            System.out.println("exception while adding the book into database ");
            e.printStackTrace();
        }
        return false;
    }

    //remove book
    public boolean bookRemove() {
        try {
            return new BookDao().delete(bookName);
        } catch (RuntimeException e) {
            System.out.println("exception while adding the book into database ");
            e.printStackTrace();
        }
        return false;
    }


    /// input for switch case 3
//    public void bookAdd() throws IllegalArgumentException {
//        try {
//            System.out.println("Enter a Book name : ");
//            String bookName = sc.nextLine();
//            System.out.println("Enter a Book Author : ");
//            String bookAuthor = sc.nextLine();
//            System.out.println("Enter a Book Category : ");
//            dispalyCategoryEnumValue();
//            String category = sc.next();
//            if (Arrays.stream(BookCategory.values()).noneMatch(enums -> enums.toString().equalsIgnoreCase(category))) {
//                sc.nextLine();
//                throw new IllegalArgumentException();
//            }
//            System.out.println("Enter a Book srno : ");
//            long srNo = sc.nextLong();
//            System.out.println("Enter a Book Copy : ");
//            int copy = sc.nextInt();
//            System.out.println(BookCategory.valueOf(category.toUpperCase()));
//            Book book = new Book(bookName, bookAuthor, BookCategory.valueOf(category.toUpperCase().trim()), srNo, copy);
//            //    new CurdOperationsBook(book).add();
//            new AdminOperation().bookInsert(book);
//            System.out.println("Book added successfully..");
//        } catch (InputMismatchException | IllegalArgumentException e) {
//            System.err.println("Enter a proper value :");
//        }
//    }


    public void updateRole() {
        try {
            System.out.println("Enter a user email: ");
            String userEmail = sc.nextLine();
            System.out.println("Enter a role : ");
            String role = sc.nextLine().toLowerCase();
            User user = Repository.usersDb.get(userEmail);
            if (Objects.nonNull(user)) {
                Role role1 = user.getRole();
                if (role1.toString().equalsIgnoreCase(role)) {
                    System.err.println("User already a " + role);
                } else {
                    user.setRole(Role.valueOf(role));
                }
            } else {
                System.out.println("User not found");
            }
        } catch (InputMismatchException | IllegalArgumentException e) {
            System.out.println("enter proper input");
        }
    }
}
