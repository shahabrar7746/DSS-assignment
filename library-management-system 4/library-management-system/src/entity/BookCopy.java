package entity;

import utils.RandomID;

import java.util.LinkedList;
import java.util.List;

public class BookCopy {
    private String CopyID;
    private Book book;
    List<Book>bookCopyDb=new LinkedList<>();

    public BookCopy(Book book) {
        CopyID = String.valueOf(new RandomID());
        this.book = book;
        bookCopyDb.add(book);
    }
}
