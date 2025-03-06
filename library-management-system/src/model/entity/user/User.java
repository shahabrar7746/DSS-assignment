package model.entity.user;

import model.entity.book.Book;
import model.entity.enums.Role;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class User {

    private String id;
    private String name;
    private String email;
    private String password;
    private Role role ;
    private List<Book>borrowedBooks;
    LocalDate dateOfJoin;
    private boolean isLogin;

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        isLogin=false;
        dateOfJoin=LocalDate.now();
        borrowedBooks=new LinkedList<>();
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public synchronized boolean setBorrowedBooksById(String id) {
     return  borrowedBooks.removeIf(book -> book.getBookId().equalsIgnoreCase(id));
    }

    public  void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    @Override
    public String toString() {
        return String.format("+-------------------------+%n" +
                        "|       User Profile      |%n" +
                        "+-------------------------+%n" +
                        "| User Id         : %-20s |%n" +
                        "| Name            : %-20s |%n" +
                        "| Email           : %-20s |%n" +
                        "| Date of Register: %-20s |%n" +
                        "| Role            : %-20s |%n" +
                        "| Borrowed Books  : %-50s |%n" +
                        "+-------------------------+",
                this.id,this.name, this.email, this.dateOfJoin,this.role,
                this.borrowedBooks.isEmpty() ? "None" : String.join(",", this.borrowedBooks.stream().map((data)-> data.getBookId()+" "+data.getName()).toArray(String[]::new)));
    }





}
