import entity.Book;
import enums.BookCategory;
import repository.dao.BookDao;
import repository.daoimpl.BookDaoImpl;
import servicesImpl.BookServicesImpl;
import ui.Home;


import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args){
BookServicesImpl bookServices = BookServicesImpl.getBookInstance();
         bookServices.addBook(new Book("The Art of Cooking", "Jane Smith", BookCategory.valueOf("COOKING"),  9));

         //        new BookServicesImpl(new BookDaoImpl()).addBook(new Book("The Art of Cooking", "Jane Smith", BookCategory.valueOf("COOKING"),  9));
//        new BookServicesImpl(new BookDaoImpl()).addBook(new Book("The History of Time", "Stephen Hawking", BookCategory.valueOf("SCIENCE"),  10));
//        new BookServicesImpl(new BookDaoImpl()).addBook(new Book("A Brief History of Time", "Stephen Hawking", BookCategory.valueOf("SCIENCE"), 10));
//        new BookServicesImpl(new BookDaoImpl()).addBook(new Book("The Alchemist", "Paulo Coelho", BookCategory.valueOf("FICTION"), 15));
//        new BookServicesImpl(new BookDaoImpl()).addBook(new Book("Sapiens: A Brief History of Humankind", "Yuval Noah Harari", BookCategory.valueOf("FICTION"), 12));
//        new BookServicesImpl(new BookDaoImpl()).addBook(new Book("The Pragmatic Programmer", "Andrew Hunt", BookCategory.valueOf("FICTION"), 8));
//        new BookServicesImpl(new BookDaoImpl()).addBook(new Book("Atomic Habits", "James Clear", BookCategory.valueOf("FICTION"), 20));
//        new BookServicesImpl(new BookDaoImpl()).addBook(new Book("1984", "George Orwell", BookCategory.valueOf("FICTION"), 18));
//        new BookServicesImpl(new BookDaoImpl()).addBook(new Book("Clean Code", "Robert C. Martin", BookCategory.valueOf("FICTION"), 6));
//        new BookServicesImpl(new BookDaoImpl()).addBook(new Book("The Psychology of Money", "Morgan Housel", BookCategory.valueOf("FICTION"), 14));
//        new BookServicesImpl(new BookDaoImpl()).addBook(new Book("To Kill a Mockingbird", "Harper Lee", BookCategory.valueOf("FICTION"), 10));
//        new BookServicesImpl(new BookDaoImpl()).addBook(new Book("Deep Work", "Cal Newport", BookCategory.valueOf("FICTION"), 12));



        new Home().HomeScreen();
    }
}