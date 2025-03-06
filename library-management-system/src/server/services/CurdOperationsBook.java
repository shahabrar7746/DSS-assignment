package server.services;

import repository.dao.BookDao;
import model.entity.book.Book;
import server.interfaces.CurdOperation;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class CurdOperationsBook extends Repository implements CurdOperation {
    private static final ReentrantLock lock = new ReentrantLock();
    private Book book;
    private String bookName;

    public CurdOperationsBook() {
    }

    public CurdOperationsBook(Book book) {
        this.book = book;
    }

    public CurdOperationsBook(String bookName) {
        this.bookName = bookName;
    }

    //add book
    @Override
    public boolean add() {
        try {
            return new BookDao().add(book);
        } catch (Exception e) {
            System.out.println("some thing went wrong... try again..");
        }
        return false;
    }

    //remove book
    @Override
    public boolean remove() {
        if (Objects.isNull(bookDb)) {
            return false;
        }
        try {
            return bookDb.removeIf((book1 -> book1.getName().trim().equalsIgnoreCase(bookName.trim())));
        } catch (Exception e) {
            System.out.println("something went wrong in remove data");
        }
        return false;
    }

    //display all books
    @Override
    public void read() {
        new BookDao().read();
    }

}
