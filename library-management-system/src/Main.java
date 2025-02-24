import entity.book.Book;
import enums.BookCategory;
import frontend.pages.Home;
import server.db.Repository;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {

        Repository.addBookDb(new Book("Java Programming", "John Doe", BookCategory.valueOf("SCIENCE"),  378328,0));
        Repository.addBookDb(new Book("The Art of Cooking", "Jane Smith", BookCategory.valueOf("COOKING"),  456789,9));
        Repository.addBookDb(new Book("The History of Time", "Stephen Hawking", BookCategory.valueOf("SCIENCE"),  123456,10));
        Repository.addBookDb(new Book("Mystery of the Nile", "Agatha Christie", BookCategory.valueOf("MYSTERY"),  789123,20));
        Repository.addBookDb(new Book("Introduction to Philosophy", "William James", BookCategory.valueOf("PHILOSOPHY"),  112233,10));
        Repository.addBookDb(new Book("The Psychology of Human Behavior", "Sigmund Freud", BookCategory.valueOf("PSYCHOLOGY"),  445566,2));
        Repository.addBookDb(new Book("Modern Romance", "Chimamanda Ngozi Adichie", BookCategory.valueOf("ROMANCE"),  789456,6));
        Repository.addBookDb(new Book("The Biography of Albert Einstein", "Walter Isaacson", BookCategory.valueOf("BIOGRAPHY"),  667788,10));
        Repository.addBookDb(new Book("Graphic Design Essentials", "David Blanchard", BookCategory.valueOf("ART"),  998877,9));
        Repository.addBookDb(new Book("Self Help for Success", "Tony Robbins", BookCategory.valueOf("SELFHELP"),  234567,16));
        Repository.addBookDb(new Book("The Great Escape", "Nelson Mandela", BookCategory.valueOf("BIOGRAPHY"),  876543,20));
        Repository.addBookDb(new Book("Adventures of Sherlock Holmes", "Arthur Conan Doyle", BookCategory.valueOf("MYSTERY"),  345678,1));
        Repository.addBookDb(new Book("The Power of Music", "Leonard Bernstein", BookCategory.valueOf("MUSIC"),  123789,8));
        Repository.addBookDb(new Book("True Crime: The Unsolved Mysteries", "Joe Kenda", BookCategory.valueOf("TRUECRIME"),  654321,7));
        Repository.addBookDb(new Book("Fantasy World", "J.R.R. Tolkien", BookCategory.valueOf("FANTASY"),  789321,7));

        Home.homeScreen();
    }
}