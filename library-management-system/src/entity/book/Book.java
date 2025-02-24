package entity.book;

import enums.BookCategory;

public class Book {

    private String bookId;
    private String name;
    private String author;
    private BookCategory category;
    private long srNo;
    private int numberOfCoupies;

    public Book(String name, String author, BookCategory category, long srNo, int numberOfCoupies) {
        this.name = name;
        this.author = author;
        this.category = category;
        this.srNo = srNo;
        this.numberOfCoupies = numberOfCoupies;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public String getBookId() {
        return bookId;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }


    public long getSrNo() {
        return srNo;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public void setSrNo(long srNo) {
        this.srNo = srNo;
    }

    public int getNumberOfCoupies() {
        return numberOfCoupies;
    }

    public void setNumberOfCoupies(int numberOfCoupies) {
        this.numberOfCoupies = numberOfCoupies;
    }

    @Override
    public int hashCode() {
        return bookId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        Book book = (Book) obj;
        if (book != null) {
            return this.bookId.equalsIgnoreCase(book.bookId);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("| %-4s | %-50s | %-32s | %-32s | %-16d | %-16d  |%n", bookId, name, category, author, srNo, numberOfCoupies);
    }


}
