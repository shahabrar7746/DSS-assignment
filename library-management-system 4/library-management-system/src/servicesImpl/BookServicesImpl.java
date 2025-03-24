package servicesImpl;

import enums.ResponseStatus;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import repository.dao.BookDao;
import repository.daoimpl.AdminDao;
import repository.daoimpl.BookDaoImpl;
import entity.Book;
import server.BookService;
import utils.Response;

import java.util.*;

public class BookServicesImpl implements BookService {
    Scanner sc = new Scanner(System.in);
   // private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final BookDao bookDao = BookDaoImpl.getBookInstance();

    private static BookServicesImpl bookInstance;


    private BookServicesImpl(){};

    public static BookServicesImpl getBookInstance(){
        if(Objects.isNull(bookInstance)){
            return new BookServicesImpl();
        }
        return bookInstance;
    }



    //remove book
    public boolean bookRemove(String srNo,int copyDelete) {
        try {
            return  bookDao.deleteBook(srNo,copyDelete);
        } catch (Exception e) {
            System.out.println("exception while adding the book into database ");
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public Response addBook(Book book) {
        Response response = new Response(null, ResponseStatus.Error, "no book found please enter a proper book");
        Book bookData=null;
        try {
            bookDao.addBook(book);
            response=new Response(bookData,ResponseStatus.SUCCESS,"Book inserted successful...");
            return response;
        } catch (Exception e) {
            System.out.println("exception while adding the book into database ");
            e.printStackTrace();
        }
        return response;
    }

    /// input for switch case 3
//    public void bookAdd() throws IllegalArgumentException {
//        try {
//            System.out.println("Enter a Book name : ");
//            String bookName = sc.nextLine();
//            System.out.println("Enter a Book Author : ");
//            String bookAuthor = sc.nextLine();
//            System.out.println("Enter a Book Category : ");
//            dispalyCategoryEnumValue();
//            String category = sc.next();
//            if (Arrays.stream(BookCategory.values()).noneMatch(enums -> enums.toString().equalsIgnoreCase(category))) {
//                sc.nextLine();
//                throw new IllegalArgumentException();
//            }
//            System.out.println("Enter a Book srno : ");
//            long srNo = sc.nextLong();
//            System.out.println("Enter a Book Copy : ");
//            int copy = sc.nextInt();
//            System.out.println(BookCategory.valueOf(category.toUpperCase()));
//            Book book = new Book(bookName, bookAuthor, BookCategory.valueOf(category.toUpperCase().trim()), srNo, copy);
//            //    new CurdOperationsBook(book).add();
//            new BookServicesImpl().bookInsert(book);
//            System.out.println("Book added successfully..");
//        } catch (InputMismatchException | IllegalArgumentException e) {
//            System.err.println("Enter a proper value :");
//        }
//    }






    @Override
    public Response DelteBook(String srNo,int noOfCopy) {
        Response response = new Response(null, ResponseStatus.Error, "No Book Found");
        if (Objects.isNull(srNo)) {
            return response;
        }
        try {
            boolean isAdded = bookDao.deleteBook(srNo, noOfCopy);
            if(isAdded){
                response = new Response(null,ResponseStatus.SUCCESS,"Book Delete Successful...");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
    @Override
    public Response UpdateBook(Book Book) {
        return null;
    }

    @Override
    public void DisplayBook() {
        List<Book> books = bookDao.getBooks();
        System.out.println(books);
        if (books.isEmpty()) {
            System.out.println(" ");
            System.out.println("+----------------+--------------------+------------+----------------+-------------------------------------------------------+----------------------------------------------------------------------------------------------+");
            System.out.println(String.format("|%-4s | %-50s | %-32s | %-15s | %-16s |%-16s |%-16s  |%-16s  |%-16s ", "Borrow ID", "BookID", "User", "Date of Borrow", "Due Date", "Return Date", "Fine", "Status", "Book Condition"));
            System.out.println("+----------------+--------------------+------------+----------------+-------------------------------------------------------+----------------------------------------------------------------------------------------------+");
            books.forEach(book -> System.out.println(book));
        } else {
            System.out.println("No Data Found");
        }
    }
    }

