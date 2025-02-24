package entity.book.category;

import entity.book.Book;
import enums.BookCategory;
import server.db.Repository;

public class Category {
    private String bookCategoryId;
    private BookCategory category;
    private long numberofBook;
    private Book book;


    public Category(String bookCategoryId, BookCategory category, long numberofBook,Book book) {
        this.bookCategoryId = bookCategoryId;
        this.category = category;
        this.numberofBook = numberofBook;
        Repository.addBook(bookCategoryId,book);
    }
}
