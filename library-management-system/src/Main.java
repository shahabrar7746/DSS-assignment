import model.entity.book.Book;
import model.entity.enums.BookCategory;
import server.services.CurdOperationsBook;
import ui.Home;


import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {

        new CurdOperationsBook(new Book("The Art of Cooking", "Jane Smith", BookCategory.valueOf("COOKING"),  456789,9)).add();
        new CurdOperationsBook(new Book("The History of Time", "Stephen Hawking", BookCategory.valueOf("SCIENCE"),  123456,10)).add();
        new CurdOperationsBook(new Book("Mystery of the Nile", "Agatha Christie", BookCategory.valueOf("MYSTERY"),  789123,20)).add();
        new CurdOperationsBook(new Book("Introduction to Philosophy", "William James", BookCategory.valueOf("PHILOSOPHY"),  112233,10)).add();
        new CurdOperationsBook(new Book("The Psychology of Human Behavior", "Sigmund Freud", BookCategory.valueOf("PSYCHOLOGY"),  445566,2)).add();
        new CurdOperationsBook(new Book("Modern Romance", "Chimamanda Ngozi Adichie", BookCategory.valueOf("ROMANCE"),  789456,6)).add();
        new CurdOperationsBook(new Book("Java Programming", "John Doe", BookCategory.valueOf("SCIENCE"),  378328,0)).add();
        new CurdOperationsBook(new Book("The Biography of Albert Einstein", "Walter Isaacson", BookCategory.valueOf("BIOGRAPHY"),  667788,10)).add();
        new CurdOperationsBook(new Book("Graphic Design Essentials", "David Blanchard", BookCategory.valueOf("ART"),  998877,9)).add();
        new CurdOperationsBook(new Book("Self Help for Success", "Tony Robbins", BookCategory.valueOf("SELFHELP"),  234567,16)).add();
        new CurdOperationsBook(new Book("The Great Escape", "Nelson Mandela", BookCategory.valueOf("BIOGRAPHY"),  876543,20)).add();
        new CurdOperationsBook(new Book("Adventures of Sherlock Holmes", "Arthur Conan Doyle", BookCategory.valueOf("MYSTERY"),  345678,1)).add();
        new CurdOperationsBook(new Book("The Power of Music", "Leonard Bernstein", BookCategory.valueOf("MUSIC"),  123789,8)).add();
        new CurdOperationsBook(new Book("True Crime: The Unsolved Mysteries", "Joe Kenda", BookCategory.valueOf("TRUECRIME"),  654321,7)).add();
        new CurdOperationsBook(new Book("Fantasy World", "J.R.R. Tolkien", BookCategory.valueOf("FANTASY"),  789321,7)).add();

        Home.homeScreen();
    }
}