package com.booking.hotelapplication.services.interfac;

import com.booking.hotelapplication.dto.Response;
import com.booking.hotelapplication.entity.Booking;

public interface IBookingService {

    Response saveBooking(Long roomId,Long userId,Booking bookingRequest);
    Response findBookingByConfirmationCode(String confirmationCode);
    Response getAllBookings();
    Response cancelBooking(Long bookingId);
}
