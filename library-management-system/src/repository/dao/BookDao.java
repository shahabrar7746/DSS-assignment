package repository.dao;

import model.entity.book.Book;

import java.time.DateTimeException;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;


public class BookDao extends DataBaseImpl {

    private static final ReentrantLock lock = new ReentrantLock();

    @Override
    public boolean add(Object object) throws RuntimeException {
        if (!(object instanceof Book book)) {
            throw new DateTimeException("Please provide book object");
        }
        if (bookDb == null) {
            bookDb = new HashSet<>();
        }
        book.setBookId(bookDb.size() + 1 + "BK");
        lock.lock();
        try {
            bookDb.add(book);
            return true;
        } catch (RuntimeException e) {
            throw new DateTimeException("Some thing happened during data addition");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void read() {
        System.out.println(" ");
        System.out.println("+----------------+--------------------+------------+----------------+-------------------------------------------------------+---------------------------+");
        System.out.println(String.format("|%-4s | %-50s | %-32s | %-32s |  %-16s |%-16s ", "BookID", "Name", "Category", "Author", "Serial Number", "Copies Available"));
        System.out.println("+----------------+--------------------+------------+----------------+-------------------------------------------------------+---------------------------+");
        if (Objects.nonNull(bookDb)) {
            bookDb.forEach(System.out::println);
        } else {
            System.out.println("No Book Found");
        }
        System.out.println(" ");
    }

    @Override
    public boolean update(Object object) {
        return false;
    }

    @Override
    public boolean delete(Object objects) {
        if (!(objects instanceof String bookName)) {
            return false;
        }
        if (Objects.isNull(bookDb)) {
            return false;
        }
        return bookDb.removeIf((book1 -> book1.getName().trim().equalsIgnoreCase(bookName.trim())));
    }
}
