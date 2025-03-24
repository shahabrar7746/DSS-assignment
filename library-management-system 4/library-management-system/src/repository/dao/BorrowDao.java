package repository.dao;

import entity.Book;
import entity.BookBorrowed;
import entity.User;

import java.util.Map;


public interface BorrowDao {

     BookBorrowed borrowBook(BookBorrowed bookBorrowed) throws Exception;

     Map<String, BookBorrowed> getBorrowBook() throws Exception;

     boolean returnBook(String bookId,User userData) throws Exception ;
}

