package com.honganhdev.lakesidehotel.service;
/*
 * @author HongAnh
 * @created 25 / 04 / 2024 - 6:11 PM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

import com.honganhdev.lakesidehotel.exception.InvalidBookingRequestException;
import com.honganhdev.lakesidehotel.exception.ResourceNotFoundException;
import com.honganhdev.lakesidehotel.model.BookedRoom;
import com.honganhdev.lakesidehotel.model.Room;
import com.honganhdev.lakesidehotel.repository.BookingRepository;
import com.honganhdev.lakesidehotel.service.serviceInterface.BookingService;
import com.honganhdev.lakesidehotel.service.serviceInterface.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private BookingRepository bookingRepository;
    private RoomService roomService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, RoomService roomService) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
    }

    //Get all booking rooms with room Id
    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long id) {
        return bookingRepository.findAllByRoomId(id);
    }

    //Get all booking rooms
    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }

    //Delete booking room with booking Id
    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    //Save a booking room
    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        //Check if OutDate before InDate will throw exception
        if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw new InvalidBookingRequestException("Check in date must come before check out date");
        }
        //Get room with room Id
        Room room = roomService.getRoomById(roomId).get();

        //Get all booking rooms with room
        List<BookedRoom> existingBookings = room.getBookings();

        //Check if room available
        boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings);

        //Add booking and save it if a room available
        if(roomIsAvailable){
            room.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        }else{
            throw new InvalidBookingRequestException("Sorry, This room is not available for the selected dates");
        }

        return bookingRequest.getBookingConfirmationCode();
    }

    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(()->new ResourceNotFoundException("No booking found with booking code :" + confirmationCode));
    }

    @Override
    public List<BookedRoom> getBookingsByUserEmail(String email) {
        return bookingRepository.findAllByGuestEmail(email);
    }

    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().isEqual(existingBooking.getCheckInDate())
                            || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                            || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                            && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                            || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                            && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                            || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                            && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                            || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                            && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                            || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                            && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
}
