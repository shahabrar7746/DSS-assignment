package controller;

import entity.Booking;
import entity.User;
import service.BookingService;

import java.util.List;

public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public void createBooking(Booking booking) {
        bookingService.createBooking(booking);
        System.out.println("\nBooking created successfully!");
    }

    public void updateBooking(Booking booking) {
        bookingService.updateBooking(booking);
        System.out.println("Booking updated successfully!");
    }

    public boolean cancelBooking(int bookingId) {
        try {
            bookingService.cancelBooking(bookingId);
            return true;
        } catch (Exception e) {
            System.out.println("Error canceling booking: " + e.getMessage());
            return false;
        }
    }

    public Booking getBookingById(int bookingId) {
        return bookingService.getBookingById(bookingId);
    }

    public List<Booking> getBookingsByUser(int userId) {
        return bookingService.getBookingsByUser(userId);
    }

    public Booking getConfirmedBookingByUserId(int loggedInGuest) {
        return bookingService.getConfirmedBookingByUserId(loggedInGuest);
    }

    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }
}
