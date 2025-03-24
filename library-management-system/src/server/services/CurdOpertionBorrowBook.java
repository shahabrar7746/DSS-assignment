package server.services;

import repository.dao.BookDao;
import repository.dao.BorrowDoa;
import model.entity.book.BookBorrowed;
import model.entity.enums.BookCondition;
import server.interfaces.CurdOperation;

import java.util.Objects;

public class CurdOpertionBorrowBook extends Repository implements CurdOperation {

   public String userID;
   public String bookId;
   public BookCondition bookCondition;
   public BookBorrowed bookBorrowed;


    public CurdOpertionBorrowBook() {
    }

    public CurdOpertionBorrowBook(String userID, String bookId, BookCondition bookCondition) {
        this.userID = userID;
        this.bookId = bookId;
        this.bookCondition = bookCondition;
    }

    public CurdOpertionBorrowBook(BookBorrowed bookBorrowed) {
        this.bookBorrowed = bookBorrowed;
    }



    //user bookBorrowed method
    @Override
    public boolean add() {

        if(Objects.isNull(bookBorrowed)){
           return false;
        }
        return new BorrowDoa().add(bookBorrowed);
    }


    @Override
    public boolean remove() {
        CurdOpertionBorrowBook curdOpertionBorrowBook = new CurdOpertionBorrowBook();
        if(Objects.isNull(curdOpertionBorrowBook))
        {
            return false;
        }
        return new BorrowDoa().delete(this);
    }

    @Override
    public void read() {
        new BookDao().read();
    }
}
