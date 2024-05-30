package com.honganhdev.lakesidehotel.service;
/*
 * @author HongAnh
 * @created 25 / 04 / 2024 - 6:10 PM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

import com.honganhdev.lakesidehotel.exception.InternalServerException;
import com.honganhdev.lakesidehotel.exception.NotFoundRoomException;
import com.honganhdev.lakesidehotel.exception.ResourceNotFoundException;
import com.honganhdev.lakesidehotel.model.Room;
import com.honganhdev.lakesidehotel.repository.RoomRepository;
import com.honganhdev.lakesidehotel.service.serviceInterface.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    //Save a room to database
    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws IOException, SQLException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        //Check file from FE not empty
        if(!file.isEmpty()){
            //Convert from bytes to blob and save it to room
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            room.setPhoto(photoBlob);
        }
        return roomRepository.save(room);
    }

    //Read distinct all room types from database
    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomType();
    }

    //Read all room from database
    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    //Get bytes array images with room Id
    @Override
    public byte[] getRoomPhotoByRoomId(Long id) throws ResourceNotFoundException, SQLException {
        //Check find a room with room Id will return null if not found a room
        Room room = roomRepository.findById(id).orElse(null);
        if(room != null){
            Blob photoBlob = room.getPhoto();
            if(photoBlob != null){
                //Convert from blob to bytes array
                return photoBlob.getBytes(1,(int) photoBlob.length());
            }
            return null;
        }
        throw new ResourceNotFoundException("Room Not Found");
    }

    //Delete room from database with room Id
    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        //If a room is present -> delete
        if(optionalRoom.isPresent()){
            roomRepository.deleteById(roomId);
        }
        else {
            throw new ResourceNotFoundException("Room Not Found");
        }
    }

    //Update a room to database
    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
        //Find room if not found will throw a exception
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()->{
            throw new ResourceNotFoundException("Room Not Found");
        });
        //Check if Room Type, Price and Photo not null
        if(roomType != null){
            room.setRoomType(roomType);
        }
        if(roomPrice != null){
            room.setRoomPrice(roomPrice);
        }
        if(photoBytes != null && photoBytes.length > 0){
            try {
                room.setPhoto(new SerialBlob(photoBytes));
            } catch (SQLException throwables) {
                throw new InternalServerException("Error updating room");
            }
        }
        return roomRepository.save(room);
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        return roomRepository.findAvailableRoomsByDatesAndType(checkInDate,checkOutDate,roomType);
    }
}
