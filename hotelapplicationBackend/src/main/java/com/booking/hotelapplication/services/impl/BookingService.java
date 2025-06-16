package com.booking.hotelapplication.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.booking.hotelapplication.dto.BookingDTO;
import com.booking.hotelapplication.dto.Response;
import com.booking.hotelapplication.entity.Booking;
import com.booking.hotelapplication.entity.Room;
import com.booking.hotelapplication.entity.User;
import com.booking.hotelapplication.exception.OurException;
import com.booking.hotelapplication.repo.BookingRepository;
import com.booking.hotelapplication.repo.RoomRepository;
import com.booking.hotelapplication.repo.UserRepository;
import com.booking.hotelapplication.services.interfac.IBookingService;
import com.booking.hotelapplication.utils.Utils;


@Service
public class BookingService implements IBookingService {


    @Autowired
    private BookingRepository bookingRepository;


    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        Response response=new Response();

        try{
            if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
                throw new OurException("Check-out date cannot be before check-in date");
            }
            Room room=roomRepository.findById(roomId).orElseThrow(()-> new OurException("Room not Found"));
            User user=userRepository.findById(userId).orElseThrow(()-> new OurException("User not found"));
            List<Booking> existingBookings = room.getBookings();
            if(!roomIsAvailable(bookingRequest,existingBookings)){
                throw new OurException("Room Not Available for selected date range");
            }
            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode= Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("Room booked successfully");
            response.setBookingConfirmationCode(bookingConfirmationCode);

        }
        catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error booking room"+e.getMessage());

        }
        return response;
    }
            
    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {

        for(Booking booking:existingBookings){
            if(booking.getCheckInDate().isBefore(bookingRequest.getCheckOutDate()) && 
               booking.getCheckOutDate().isAfter(bookingRequest.getCheckInDate())){
                return false; // Room is already booked for the requested date range
            }
         }
        return true;
    }
            
    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response=new Response();
        
        try{
            Booking booking= bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(()->new OurException("Cannot found booking with confirmation code: "+confirmationCode));
            BookingDTO bookingDTO=Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking,true);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBooking(bookingDTO);
            
        }
        catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error booking room"+e.getMessage());

        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response=new Response();
        try{
            List<Booking> bookingList= bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList=Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingList(bookingDTOList);
        }
        catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting bookings"+e.getMessage());

        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response=new Response();
        try{
            bookingRepository.findById(bookingId).orElseThrow(()->new OurException("Cannot found booking with id: "+bookingId));
            bookingRepository.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("successful");
        }
        catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting to cancel booking"+e.getMessage());

        }
        return response;
    }

}
