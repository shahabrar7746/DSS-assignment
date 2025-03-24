//package servicesImpl;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import repository.daoimpl.BookDaoImpl;
//import entity.Book;
//import server.LibraryServices;
//
//import java.util.concurrent.locks.ReentrantLock;
//
//public class CurdOperationsBook implements LibraryServices {
//    private static final ReentrantLock lock = new ReentrantLock();
//    private Book book;
//    private String bookName;
//    Logger logger = LoggerFactory.getLogger(CurdOperationsBook.class);
//
//    public CurdOperationsBook() {
//    }
//
//    public CurdOperationsBook(Book book) {
//        this.book = book;
//    }
//
//    public CurdOperationsBook(String bookName) {
//        this.bookName = bookName;
//    }
//
//    //add book
//    @Override
//    public boolean add() {
//        try {
//            return new BookDaoImpl().add(book);
//        } catch (Exception e) {
//            logger.error("Error in add");
//            System.out.println("some thing went wrong... try again..");
//        }
//        return false;
//    }
//
//    //remove book
//    @Override
//    public boolean remove() {
//        return new BookDaoImpl().delete(bookName);
//    }
//
//    //display all books
//    @Override
//    public void read() {
//        new BookDaoImpl().read();
//    }
//
//}
