package repository.dao;

import repository.DataBase;
import model.entity.book.Book;
import model.entity.book.BookBorrowed;
import model.entity.enums.Role;
import model.entity.user.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class DataBaseImpl implements DataBase {

    static final String adminEmail = "admin@gmail.com";
    static final String adminPassword = "1234";

    //list of category database
    static HashMap<String, List<Book>> bookCategoryDb;
    //list of book database
    static HashSet<Book> bookDb;
    //list of user database
    static HashMap<String, User> usersDb;
    //list of borrow database
     static ConcurrentHashMap<String, BookBorrowed> borrowDataDb = new ConcurrentHashMap<>();
    static {
        usersDb = new HashMap<>();
        User adminUser = new User("admin", adminEmail, adminPassword, Role.admin);
        adminUser.setId("Super01");
        usersDb.put(adminEmail, adminUser);
    }
}
