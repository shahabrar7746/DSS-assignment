package entity.book;

import enums.BookCondition;
import enums.Status;

import java.time.LocalDate;

public class BookBorrowed {

    private String borrowId;
    private String bookId;
    private String userId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Status status;
    private double fine;
    private BookCondition bookCondition;

    public BookBorrowed( String bookId, String userId, LocalDate borrowDate, BookCondition bookCondition) {
        this.bookId = bookId;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(8);
        this.returnDate = borrowDate.plusDays(9);
        this.status = Status.Borrowed;
        this.fine = 0.0;
        this.bookCondition = bookCondition;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BookCondition getBookCondition() {
        return bookCondition;
    }

    public void setBookCondition(BookCondition bookCondition) {
        this.bookCondition = bookCondition;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;

    }

    @Override
    public String toString() {
        return String.format("| %-4s | %-50s | %-32s | %-10s | %-16s | %-16s | %-16s | %-16s | %-16s |%n", borrowId,bookId, userId,borrowDate,dueDate ,returnDate,fine,status,bookCondition);
    }
}
