package com.honganhdev.lakesidehotel.controller;
/*
 * @author HongAnh
 * @created 25 / 04 / 2024 - 6:07 PM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

import com.honganhdev.lakesidehotel.exception.InvalidBookingRequestException;
import com.honganhdev.lakesidehotel.exception.ResourceNotFoundException;
import com.honganhdev.lakesidehotel.model.BookedRoom;
import com.honganhdev.lakesidehotel.model.Room;
import com.honganhdev.lakesidehotel.response.BookingResponse;
import com.honganhdev.lakesidehotel.response.RoomResponse;
import com.honganhdev.lakesidehotel.service.serviceInterface.BookingService;
import com.honganhdev.lakesidehotel.service.serviceInterface.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private BookingService bookingService;
    private RoomService roomService;

    @Autowired
    public BookingController(BookingService bookingService, RoomService roomService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
    }

    //Get all bookings room from database
    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings(){
        //Get all booked rooms
        List<BookedRoom> bookings = bookingService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        // Convert from booked rooms to the booked room response
        bookingResponses.addAll(bookings.stream()
                .map((bookedRoom -> getBookingResponse(bookedRoom)))
                .toList());

        return ResponseEntity.ok(bookingResponses);
    }

    //Get booking room from database with confirmation code
    @GetMapping("/confirmation/{code}")
    public ResponseEntity<?> getBookingsByConfirmationCode(@PathVariable(name = "code") String confirmationCode){
        try{
            //Get booking room with confirmation code from database
            BookedRoom booking = bookingService.findByBookingConfirmationCode(confirmationCode);
            //Convert to booking room response
            BookingResponse bookingResponse = getBookingResponse(booking);
            //Return status 200 if found room
            return ResponseEntity.ok(bookingResponse);
        }catch (ResourceNotFoundException ex){
            //Return not found if not found room
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    //Save booking room to the database
    @PostMapping("/room/{id}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable(name = "id") Long roomId,
                                         @RequestBody BookedRoom bookingRequest){
        try{
            //Return confirmation code after save booking room
            String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);
            //Status 200 if no exception
            return ResponseEntity.ok("Room booked successfully, Your booking confirmation code is: "+confirmationCode);
        }catch (InvalidBookingRequestException ex){
            //Status 400 if has exception
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    //Delete booking room from database with Id
    @DeleteMapping("/booking/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void cancelBooking(@PathVariable(name = "id") Long bookingId){
        //Cancel booking room
        bookingService.cancelBooking(bookingId);
    }

    //Convert from booked room to booking response
    private BookingResponse getBookingResponse(BookedRoom bookedRoom) {
        //Find room from booked room
        Room room = roomService.getRoomById(bookedRoom.getRoom().getId()).get();
        //Convert from room to room response
        RoomResponse roomResponse = new RoomResponse(room.getId(), room.getRoomType(), room.getRoomPrice());

        return new BookingResponse(
                bookedRoom.getBookingId(),
                bookedRoom.getCheckInDate(),
                bookedRoom.getCheckOutDate(),
                bookedRoom.getGuestFullName(),
                bookedRoom.getGuestEmail(),
                bookedRoom.getNumOfAdults(),
                bookedRoom.getNumOfChildren(),
                bookedRoom.getTotalNumOfGuest(),
                bookedRoom.getBookingConfirmationCode(),
                roomResponse
        );
    }
}
