package com.honganhdev.lakesidehotel.controller;
/*
 * @author HongAnh
 * @created 25 / 04 / 2024 - 6:07 PM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

import com.honganhdev.lakesidehotel.exception.PhotoRetrievalException;
import com.honganhdev.lakesidehotel.exception.ResourceNotFoundException;
import com.honganhdev.lakesidehotel.model.BookedRoom;
import com.honganhdev.lakesidehotel.model.Room;
import com.honganhdev.lakesidehotel.response.BookingResponse;
import com.honganhdev.lakesidehotel.response.RoomResponse;
import com.honganhdev.lakesidehotel.service.serviceInterface.BookingService;
import com.honganhdev.lakesidehotel.service.serviceInterface.RoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;
    private final BookingService bookingService;

    //Create room and save it to database
    @PostMapping("/add/new-room")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> addNewRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException {

        Room savedRoom = roomService.addNewRoom(photo,roomType,roomPrice);
        //Convert from room to room response
        RoomResponse roomResponse = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(),savedRoom.getRoomPrice());
        return ResponseEntity.ok(roomResponse);
    }

    //Read all room types from database
    @GetMapping("/room/types")
    public List<String> getRoomType(){
        return roomService.getAllRoomTypes();
    }

    //Read all rooms from database
    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms(){
        //Get rooms from database
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = new ArrayList<>();
        //Convert from room to room response
        rooms.forEach(room -> {
            //Create bytes arrays to save images
            byte[] photoBytes = new byte[0];
            try {
                //Get images with room id
                photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if(photoBytes != null && photoBytes.length > 0){
                //Convert to string with base 64
                String base64String = Base64.encodeBase64String(photoBytes);
                //Convert from room to room response
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setPhoto(base64String);
                //Add to List
                roomResponses.add(roomResponse);
            }
        });
        return ResponseEntity.ok().body(roomResponses);
    }

    //Delete room from database with id
    @DeleteMapping("/delete/room/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable(name = "id") Long roomId){
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Updated room with room id
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable(name = "id") Long roomId,
                                                   @RequestParam(required = false) String roomType,
                                                   @RequestParam(required = false) BigDecimal roomPrice,
                                                   @RequestParam(required = false) MultipartFile photo) throws IOException, SQLException {

        //If photo not null and not empty so will return bytes arrays else will get Images from database with room Id
        byte[] photoBytes = photo != null && !photo.isEmpty() ?
                photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);

        //Convert to Blob if bytes arrays not null or empty
        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;

        Room room = roomService.updateRoom(roomId,roomType,roomPrice,photoBytes);
        room.setPhoto(photoBlob);

        //Convert from room to room response
        RoomResponse roomResponse = getRoomResponse(room);
        return ResponseEntity.ok(roomResponse);
    }

    //Get room with room id
    @GetMapping("/room/{id}")
    public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable(name = "id") Long roomId){
        //Get room with id to optional
        Optional<Room> theRoom = roomService.getRoomById(roomId);

        //Convert to room response
        return theRoom.map((room)->{
            RoomResponse roomResponse = getRoomResponse(room);
            return ResponseEntity.ok(Optional.of(roomResponse));
        }).orElseThrow(() ->{
            //throw exception if cannot found room
            throw new ResourceNotFoundException("Room not found");
        });
    }

    @GetMapping("/search-rooms")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(
            @RequestParam(name = "checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(name = "checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(name = "roomType") String roomType) throws SQLException {
        List<Room> availableRooms = roomService.getAvailableRooms(checkInDate,checkOutDate,roomType);
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room availableRoom : availableRooms) {
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(availableRoom.getId());
            if(photoBytes != null && photoBytes.length > 0){
                String photoBase64 = Base64.encodeBase64String(photoBytes);
                RoomResponse roomResponse = getRoomResponse(availableRoom);
                roomResponse.setPhoto(photoBase64);
                roomResponses.add(roomResponse);
            }
        }
        if(roomResponses.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(roomResponses);
    }

    private RoomResponse getRoomResponse(Room room) {
//        List<BookedRoom> bookings = bookingService.getAllBookingsByRoomId(room.getId());
//        List<BookingResponse> bookingResponses = bookings.stream().map(booking -> new BookingResponse(booking.getBookingId(),
//                booking.getCheckInDate(),
//                booking.getCheckOutDate(),
//                booking.getBookingConfirmationCode()))
//                .collect(Collectors.toList());

        byte[] photoBytes = null;
        //Get photo from room
        Blob photoBlob = room.getPhoto();
        if(photoBlob != null){
            try {
                //Convert from blob to bytes array
                photoBytes = photoBlob.getBytes(1,(int) photoBlob.length());
            } catch (SQLException throwables) {
                throw new PhotoRetrievalException("Error retrieval photo");
            }
        }
        return new RoomResponse(room.getId(),
                room.getRoomType(),
                room.getRoomPrice(),
                room.isBooked(),
                photoBytes);
    }
}
