package repository.dao;

import entity.Book;
import utils.Response;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Book addBook(Book book) throws Exception;

    boolean deleteBook(String SrNo, int noOfCopyDelete) throws Exception;

    List<Book> getBooks();

    public Optional<Book> findBookById(String id);
}
