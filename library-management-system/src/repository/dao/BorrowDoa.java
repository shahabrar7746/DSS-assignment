package repository.dao;

import model.entity.book.Book;
import model.entity.book.BookBorrowed;
import model.entity.enums.Status;
import model.entity.user.User;
import server.services.CurdOpertionBorrowBook;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class BorrowDoa extends DataBaseImpl {
    private static final ReentrantLock lock = new ReentrantLock();


    @Override
    public boolean add(Object object) throws RuntimeException {
        if (!(object instanceof BookBorrowed)) {
            return false;
        }
        BookBorrowed bookBorrowed = (BookBorrowed) object;
        if ((bookDb.stream().anyMatch(book -> book.getBookId().equalsIgnoreCase(bookBorrowed.getBookId())) &&
                usersDb.entrySet().stream().anyMatch(user -> user.getValue().getId().equalsIgnoreCase(bookBorrowed.getUserId())))) {
            //setting the id for borrowed
            int id = borrowDataDb.size() + 1;
            bookBorrowed.setBorrowId("br" + id);
            borrowDataDb.put(bookBorrowed.getBookId(), bookBorrowed);
            //check for the copy of book is present in the db or not if present then check for the copy it should have at least 1 copy
            if (bookDb.stream().filter(book -> bookBorrowed.getBookId().equalsIgnoreCase(book.getBookId())).allMatch(bookFilter -> bookFilter.getNumberOfCoupies() >= 1)) {
                //try to get book object from the db for decrement the copy by 1
                Optional<Book> first = bookDb.stream().filter(book -> book.getBookId().equalsIgnoreCase(bookBorrowed.getBookId())).findFirst();
                first.ifPresent(book -> book.setNumberOfCoupies(book.getNumberOfCoupies() - 1));
                //getting the book object form the book db
                List<Book> list = bookDb.stream().filter(book -> book.getBookId().equalsIgnoreCase(bookBorrowed.getBookId())).collect(Collectors.toList());
                List<Map.Entry<String, User>> collect = usersDb.entrySet().stream().filter(data -> data.getValue().getId().equalsIgnoreCase(bookBorrowed.getUserId())).collect(toList());
                //updating the user object borrowed book list/adding the borrowed book into that
                for (Map.Entry<String, User> userData : collect) {
                    if (userData.getValue().getId().equalsIgnoreCase(bookBorrowed.getUserId())) {
                        userData.getValue().setBorrowedBooks(list);
                    }
                }
                return true;
            } else {
                System.out.println("no copy of book is available for borrowed");
            }
        } else {
            System.out.println("user not found");
        }
        return false;
    }

    @Override
    public void read() {
        if (!borrowDataDb.isEmpty()) {
            System.out.println(" ");
            System.out.println("+----------------+--------------------+------------+----------------+-------------------------------------------------------+----------------------------------------------------------------------------------------------+");
            System.out.println(String.format("|%-4s | %-50s | %-32s | %-15s | %-16s |%-16s |%-16s  |%-16s  |%-16s ", "Borrow ID", "BookID", "User", "Date of Borrow", "Due Date", "Return Date", "Fine", "Status", "Book Condition"));
            System.out.println("+----------------+--------------------+------------+----------------+-------------------------------------------------------+----------------------------------------------------------------------------------------------+");
            borrowDataDb.forEach((key, value) -> System.out.println(value));
        } else {
            System.out.println("No Data Found");
        }
    }

    @Override
    public boolean update(Object object) {
        return false;
    }

    @Override
    public boolean delete(Object object) {
        if (!(object instanceof CurdOpertionBorrowBook)) {
            return false;
        }
        CurdOpertionBorrowBook bookData = (CurdOpertionBorrowBook) object;
        System.out.println(bookData.bookId);
        BookBorrowed data = borrowDataDb.get(bookData.bookId);
        boolean isFine;
        if (usersDb.values().stream().filter(user -> user.getId().equalsIgnoreCase(bookData.userID)).
                allMatch(isBorrowed -> isBorrowed.getBorrowedBooks().isEmpty())) {
            return false;
        }
        if (Objects.nonNull(data)) {
            if (!bookData.bookCondition.equals(borrowDataDb.get(bookData.bookId).getBookCondition())) {
                isFine = fineCalculate(data.getFine(), data);
            } else {
                isFine = false;
            }
            return usersDb.values().stream().filter(user -> user.getId().
                    equalsIgnoreCase(bookData.userID)).anyMatch(userSet -> {
                if (isFine)
                    System.out.println("you have cross the date of return i.e the late fess you have to pay :" +
                            borrowDataDb.get(bookData.bookId).getFine());
                borrowDataDb.get(bookData.bookId).setStatus(Status.Returned);
                return userSet.setBorrowedBooksById(bookData.bookId);
            });
        }
        return false;
    }

    //calculate the fine for late due
    private static boolean fineCalculate(double fine, BookBorrowed data) {
        long fineAmount = 0;
        if (data.getReturnDate().isBefore(LocalDate.now())) {
            fineAmount = ChronoUnit.DAYS.between(data.getReturnDate(), LocalDate.now());
            fine += fineAmount * 50;
        }
        fine += fineAmount + 100;
        lock.lock();
        try {
            data.setFine(fine);
        } finally {
            lock.unlock();
        }
        return true;
    }
}



