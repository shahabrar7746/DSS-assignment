package entity;

import enums.BookCategory;
import utils.RandomID;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Book {

    private String bookId;
    private String name;
    private String author;

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Book book = (Book) object;
        return Objects.equals(bookId, book.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bookId);
    }

    private BookCategory category;
    private List<String> srNo =new LinkedList<>();
    private int numberOfCoupies;

    public Book(String name, String author, BookCategory category, int numberOfCoupies) {
        this.name = name;
        this.author = author;
        this.category = category;
        this.numberOfCoupies = numberOfCoupies;
        copyId(numberOfCoupies);
    }

    private void copyId(int numberOfCoupies){
        for(int i=0;i<numberOfCoupies;i++){
              srNo.add(new RandomID().srno());
        }
    }

    public List<String> getSrNo() {
        return srNo;
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

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public int getNumberOfCoupies() {
        return numberOfCoupies;
    }

    public void setNumberOfCoupies(int numberOfCoupies) {
        this.numberOfCoupies = numberOfCoupies;
    }

    @Override
    public String toString() {
        return String.format("| %-4s | %-50s | %-32s | %-32s | %-16d | %-16d  |%n", bookId, name, category, author, numberOfCoupies);
    }


}
