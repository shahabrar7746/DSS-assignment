package servicesImpl;

import entity.BookBorrowed;
import entity.User;
import enums.ResponseStatus;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import repository.dao.BorrowDao;
import server.BorrowedBookServices;
import utils.Response;
import java.util.Objects;

public class BorrowedBookServicesImpl implements BorrowedBookServices {

    // private static final Logger log = LoggerFactory.getLogger(BorrowedBookServicesImpl.class);
    private final BorrowDao borrowDao;

    public BorrowedBookServicesImpl(BorrowDao borrowDao) {
        this.borrowDao = borrowDao;
    }

    @Override
    public Response borrowBook(BookBorrowed bookBorrowed) {
        Response response = new Response(null, ResponseStatus.Error, "something went wrong");
        if (Objects.isNull(bookBorrowed)) {
            return null;
        }
        try {
            response = new Response(bookBorrowed, ResponseStatus.SUCCESS, "Book Borrowed Successful");
        } catch (Exception e) {
            //  log.error("error at book borrowed ",e);
        }
        return response;
    }


    @Override
    public Response returnBook(String bookId, User user) {
        Response response = new Response(null, ResponseStatus.Error, "something went wrong");
        try {
            if (borrowDao.returnBook(bookId, user)) {
                //   response = new Response(, ResponseStatus.Error, "something went wrong");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}