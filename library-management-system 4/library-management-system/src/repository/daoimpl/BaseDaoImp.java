package repository.daoimpl;

import customexception.DataDeleteException;
import customexception.DataInsertException;
import customexception.DataReadException;
import customexception.DataUpdateException;
import entity.Book;
import entity.BookBorrowed;
import entity.User;
import enums.Role;
import repository.DataBase;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseDaoImp implements DataBase {
    private static final String adminEmail = "admin@gmail.com";
    private static final String adminPassword = "1234";

   // list of category database
   private   Map<String, List<Book>> bookCategoryDb;

   // list of user database
    private  Map<String, User> usersDb = new ConcurrentHashMap<>();
   // list of borrow database
    private  Map<String, BookBorrowed> borrowDataDb = new ConcurrentHashMap<>();

    static {
        User adminUser = new User("admin", adminEmail, adminPassword, Role.admin);
        adminUser.setId("Super01");
        new BaseDaoImp().usersDb.put(adminEmail, adminUser);
    }

    @Override
    public boolean add(Object object) throws DataInsertException {
        return false;
    }

    @Override
    public void read() throws DataReadException {

    }

    @Override
    public boolean update(Object object) throws DataUpdateException {
        return false;
    }

    @Override
    public boolean delete(Object object) throws DataDeleteException {
        return false;
    }
}
