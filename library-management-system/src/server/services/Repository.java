package server.services;

import model.entity.book.Book;
import model.entity.book.BookBorrowed;
import model.entity.enums.Role;
import model.entity.user.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public abstract class Repository  {
     static final String adminEmail = "admin@gmail.com";
     static final String adminPassword = "1234";


    //list of category database
     static HashMap<String, List<Book>> bookCategoryDb;
    //list of book database
     static HashSet<Book> bookDb;
    //list of user database
     static HashMap<String, User> usersDb;
    //list of borrow database
   //  static HashMap<String, BookBorrowed> borrowDataDb = new HashMap<>();

    //for load and initialize the admin in to  database
    static {
        usersDb = new HashMap<>();
        User adminUser = new User("admin", adminEmail, adminPassword, Role.admin);
        adminUser.setId("Super01");
        usersDb.put(adminEmail, adminUser);
    }


    //admin remove user


}

