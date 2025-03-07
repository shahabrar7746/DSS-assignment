import controller.BookingController;
import controller.InvoiceController;
import controller.RoomController;
import controller.UserController;
import dao.BookingDaoImpl;
import dao.InvoiceDaoImpl;
import dao.RoomDaoImpl;
import dao.UserDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.*;
import view.AdminDashBoard;
import view.Menu;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(Main.class);
        logger.error("Test");
        Scanner scanner = new Scanner(System.in);

        RoomService roomService = new RoomServiceImpl(new RoomDaoImpl());
        UserService userService = new UserServiceImpl(new UserDaoImpl());
        BookingService bookingService = new BookingServiceImpl(new BookingDaoImpl());
        InvoiceService invoiceService = new service.InvoiceServiceImpl(new InvoiceDaoImpl());

        RoomController roomController = new RoomController(roomService);
        UserController userController = new UserController(userService);
        BookingController bookingController = new BookingController(bookingService);
        InvoiceController invoiceController = new InvoiceController(invoiceService);

        AdminDashBoard adminDashBoard = new AdminDashBoard( roomController, userController, bookingController, invoiceController, scanner);

        Menu menu = new Menu(roomController, userController, bookingController, invoiceController, adminDashBoard);
        menu.displayMainMenu();
    }
}
