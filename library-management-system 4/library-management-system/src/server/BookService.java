package server;

import entity.Book;
import utils.Response;

public interface BookService {
    Response addBook(Book book);
    Response DelteBook(String srNo,int noOfCopy);
    Response UpdateBook(Book Book);
    void DisplayBook();
}
