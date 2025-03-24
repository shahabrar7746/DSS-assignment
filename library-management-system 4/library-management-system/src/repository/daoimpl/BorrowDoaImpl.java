package repository.daoimpl;

import entity.Book;
import entity.BookBorrowed;
import entity.User;
import repository.dao.BookDao;
import repository.dao.BorrowDao;
import repository.dao.UserDao;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BorrowDoaImpl implements BorrowDao {
    private Map<String, BookBorrowed> borrowDataDb = new HashMap<>();
    private final BookDao bookDao;
    private final UserDao user;

    public BorrowDoaImpl(BookDao bookDao, UserDao user) {
        this.bookDao = bookDao;
        this.user = user;
    }


    @Override
    public BookBorrowed borrowBook(BookBorrowed bookBorrowed) throws Exception {
        Optional<Book> book = bookDao.findBookById(bookBorrowed.getBookId());
        if (Objects.isNull(bookDao.findBookById(bookBorrowed.getBookId()))) {
            return bookBorrowed;
        }
        try {
            Optional<User> userById = user.findUserById(bookBorrowed.getUserId());
            if (userById.isPresent()) {
                int id = borrowDataDb.size() + 1;
                bookBorrowed.setBorrowId("br" + id);
                borrowDataDb.put(bookBorrowed.getBookId(), bookBorrowed);
                if (bookDao.findBookById(bookBorrowed.getBookId()).stream().anyMatch(bookCopyCheck -> bookCopyCheck.getNumberOfCoupies() >= 1)) {
                    book.ifPresent(bookCopy -> bookCopy.setNumberOfCoupies(bookCopy.getNumberOfCoupies() - 1));
                    if (book.isPresent()) {
                        userById.stream().map(user -> user.setBorrowedBooksById(book.toString()));
                    }
                    return bookBorrowed;
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return null;
    }

    @Override
    public Map<String, BookBorrowed> getBorrowBook() {
        return borrowDataDb;
    }

    @Override
    public boolean returnBook(String bookId, User userData) throws Exception {
        try {
            if (borrowDataDb.containsKey(bookId)) {
                BookBorrowed borrowedBook = borrowDataDb.get(bookId);
                if (borrowedBook.getUserId().equalsIgnoreCase(userData.getId())) {
                    List<Book> borrowedBooksList = userData.getBorrowedBooks();
                    fineCalculate(borrowedBook.getFine(), borrowedBook);
                    return borrowedBooksList.remove(bookId);
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return false;
    }

    //calculate the fine for late due
    private void fineCalculate(double fine, BookBorrowed bookBorrowed) {
        long fineAmount = 0;
        if (!bookBorrowed.getReturnDate().isBefore(LocalDate.now())) {
            return;
        }
        fineAmount = ChronoUnit.DAYS.between(bookBorrowed.getReturnDate(), LocalDate.now());
        fine += fineAmount * 50;
        bookBorrowed.setFine(fine);
    }
}



