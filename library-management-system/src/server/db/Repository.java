package server.db;

import entity.book.Book;
import entity.book.BookBorrowed;
import enums.BookCondition;
import enums.Status;
import enums.Role;
import entity.user.User;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Repository {
    private static final Logger logger = Logger.getLogger(Repository.class.getName());
    private static final String adminEmail = "admin@gmail.com";
    private static final String adminPassword = "1234";
    private static final ReentrantLock lock = new ReentrantLock();

    //list of category database
    private static HashMap<String, List<Book>> bookCategoryDb;
    //list of book database
    private static HashSet<Book> bookDb;
    //list of user database
    private static HashMap<String, User> usersDb;
    //list of borrow database
    private static HashMap<String, BookBorrowed> borrowDataDb = new HashMap<>();

    //for load and initialize the admin in to  database
    static {
        usersDb = new HashMap<>();
        User adminUser = new User("admin", adminEmail, adminPassword, Role.Admin);
        adminUser.setId("Super01");
        usersDb.put(adminEmail, adminUser);
    }

    //constructor private
    private Repository() {
    }

    /// *************************implementation of add or remove**********************

    //user add registration signup
    public static void userRegistration(User user) throws IOException {
        if (Objects.isNull(usersDb)) {
            usersDb = new HashMap<>();
        }
        if (Objects.nonNull(user)) {
            if (!usersDb.containsKey(user.getEmail()) && !user.getEmail().equalsIgnoreCase(adminEmail)) {
                //setting the user id
                int idNumber = usersDb.size() + 1;
                String id = "user" + idNumber;
                try {
                    user.setId(id.trim());
                    user.setPassword((user.getPassword().hashCode()+("password").hashCode()+"xyz"));
                    lock.lock();
                    usersDb.put(user.getEmail(), user);
                } finally {
                    lock.unlock();
                }
                logManage("\n"+"Name :" + user.getName() + "\n" + "Email :" + user.getEmail() + "\n" + "Password :" + "**********" + "\n" + "Role :" + user.getRole() + "\n" + "Date: " + LocalDate.now() + "\n" + "****************************");
            } else {
                logger.info("user already exits....");
            }
        } else {
            logger.info("Fill Credentials Properly...");
        }
    }

    //user login
    public static User userLogin(String email, String password) {
        if (email != null && password != null) {
            try {
                if (email.equalsIgnoreCase(adminEmail) || usersDb.containsKey(email)) {
                    User user = usersDb.get(email);
                    if (user.getPassword().equals(password.hashCode()+("password").hashCode()+"xyz") || password.equalsIgnoreCase(adminPassword)) {
                        user.setLogin(true);
                        return user;
                    } else {
                        return null;
                    }
                }
            } catch (Exception e) {
                logger.info("User Not Found");
            }
        }
        return null;
    }

    //Method to Add Books
    public static void addBookDb(Book book) {
        if (bookDb == null) {
            bookDb = new HashSet<>();
        }
        book.setBookId(bookDb.size() + 1 + "BK");
        lock.lock();
        try {
            bookDb.add(book);
        } finally {
            lock.unlock();
        }
    }

    // add book in category
    public static void addBook(String category, Book book) {
        if (bookCategoryDb == null) {
            bookCategoryDb = new HashMap<>();
        }
        if (bookCategoryDb.containsKey(category)) {
            List<Book> listOfCategory = bookCategoryDb.get(category);
            if (!listOfCategory.contains(book.getBookId())) {
                listOfCategory.add(book);
                logger.info("successful Book Added");
            } else {
                logger.info("Book already present");
            }
        }
        //add new category
        else {
            ArrayList<Book> listOfBook = new ArrayList<>();
            listOfBook.add(book);
            bookCategoryDb.put(category, listOfBook);
        }
    }

    //remove book
    public static boolean removeBook(String bookName) {
        if (Objects.isNull(bookDb)) {
            return false;
        }
        return bookDb.removeIf((book -> book.getName().trim().equalsIgnoreCase(bookName.trim())));
    }

    //get all books
    public static void printBooks() {
        System.out.println(" ");
        System.out.println("+----------------+--------------------+------------+----------------+-------------------------------------------------------+---------------------------+");
        System.out.println(String.format("|%-4s | %-50s | %-32s | %-32s |  %-16s |%-16s ", "BookID", "Name", "Category", "Author", "Serial Number", "Copies Available"));
        System.out.println("+----------------+--------------------+------------+----------------+-------------------------------------------------------+---------------------------+");
        bookDb.forEach(System.out::println);
        System.out.println(" ");
    }

    //book borrow
    public static boolean printBorrowBook(BookBorrowed bookBorrowed) {
        if (Objects.nonNull(bookBorrowed) && (bookDb.stream().anyMatch(book -> book.getBookId().equalsIgnoreCase(bookBorrowed.getBookId())) && usersDb.entrySet().stream().anyMatch(user -> user.getValue().getId().equalsIgnoreCase(bookBorrowed.getUserId())))) {
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
                logger.info("no copy of book is available for borrowed");
            }
        } else {
            logger.info("user not found");
        }
        return false;
    }

    //book return
    public static boolean returnBook(String userID, String bookId, BookCondition bookCondition) {
        BookBorrowed data = borrowDataDb.get(bookId);
        boolean isFine;
        //check for the user which he request to return is present in database or not or if present he borrowed that book or not
        if (usersDb.values().stream().filter(user -> user.getId().equalsIgnoreCase(userID)).allMatch(isBorrowed -> isBorrowed.getBorrowedBooks().isEmpty())) {
            return false;
        }
        //if the book is present in the user object
        if (Objects.nonNull(data)) {
            if (!bookCondition.equals(borrowDataDb.get(bookId).getBookCondition())) {
                long fineAmount = 0;
                double fine = data.getFine();
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
                isFine = true;
            } else {
                isFine = false;
            }
            return usersDb.values().stream().filter(user -> user.getId().equalsIgnoreCase(userID)).anyMatch(userSet -> {
                if (isFine)
                    logger.info("you have cross the date of return i.e the late fess you have to pay :" + borrowDataDb.get(bookId).getFine());
                lock.lock();
                try {
                    borrowDataDb.get(bookId).setStatus(Status.Returned);
                } finally {
                    lock.unlock();
                }
                return userSet.setBorrowedBooksById(bookId);
            });
        }
        return false;
    }

    //borrowed Data
    public static void getBorrowedData() {
        if (!borrowDataDb.isEmpty()) {
            System.out.println(" ");
            System.out.println("+----------------+--------------------+------------+----------------+-------------------------------------------------------+----------------------------------------------------------------------------------------------+");
            System.out.println(String.format("|%-4s | %-50s | %-32s | %-15s | %-16s |%-16s |%-16s  |%-16s  |%-16s ", "Borrow ID", "BookID", "User", "Date of Borrow", "Due Date", "Return Date", "Fine", "Status", "Book Condition"));
            System.out.println("+----------------+--------------------+------------+----------------+-------------------------------------------------------+----------------------------------------------------------------------------------------------+");
            borrowDataDb.forEach((key, value) -> System.out.println(value));
        } else {
            logger.info("No Data Found");
        }
    }

    //list of user
    public static void diplayAllUser() {
        usersDb.forEach((email, user) -> System.out.println(user));
    }

    //admin remove user
    public static boolean deleteUser(String email) {
        //if admin try to delete itself then it returns false
        if (email.equalsIgnoreCase(adminEmail)) {
            logger.info("Invalid operation ");
            return false;
        }
        //this is for user deletion
        if (Objects.nonNull(usersDb.get(email))) {
            if (usersDb.entrySet().stream().filter(users -> users.getKey().equalsIgnoreCase(email)).anyMatch(book -> book.getValue().getBorrowedBooks().isEmpty())) {
                return usersDb.entrySet().removeIf(user -> user.getKey().equalsIgnoreCase(email));
            } else {
                logger.info("user not return Borrowed Books first return book then try to delete user");
            }
        } else {
            logger.info("Invalid email/User not found");
        }
        return false;
    }

    //Log manager for new Users
    private static void logManage(String data) throws IOException {
        try (FileWriter fileWriter = new FileWriter("src/server/log/Log", true)) {
            BufferedWriter bufferedWriter= new BufferedWriter(fileWriter);
            lock.lock();
            try {
                System.out.println("data writing");
                bufferedWriter.write(data + " ");
                bufferedWriter.flush();
            } finally {
                lock.unlock();
            }
        } catch (IOException e) {
            throw new IOException(e);
        }

    }
//        //print all category
//    public static void printCategoryBook(String category) {
//        List<Book> data = bookCategoryDb.get(category);
//        if (data != null) {
//            for (Book book : data) {
//                logger.info(book.getBookId());
//            }
//        } else {
//            logger.info("no data found");
//        }
//    }
}

