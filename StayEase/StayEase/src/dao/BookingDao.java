package dao;

import entity.Booking;

import java.util.List;

public interface BookingDao {
    void createBooking(Booking booking);

    void updateBooking(Booking booking);

    void cancelBooking(int bookingId);

    Booking getBookingById(int bookingId);

    List<Booking> getBookingsByUser(int userId);

    Booking getConfirmedBookingByUserId(int userId);

    List<Booking> getAllBookings();
}
