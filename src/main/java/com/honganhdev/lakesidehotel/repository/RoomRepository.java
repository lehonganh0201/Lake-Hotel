package com.honganhdev.lakesidehotel.repository;
/*
 * @author HongAnh
 * @created 25 / 04 / 2024 - 6:08 PM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

import com.honganhdev.lakesidehotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
    @Query(value = "SELECT DISTINCT r.roomType FROM Room r")
    public List<String> findDistinctRoomType();

    @Query(value = "SELECT DISTINCT r FROM Room r " +
            "WHERE r.roomType LIKE %:roomType% " +
            "AND r.id NOT IN " +
            "(SELECT br.room.id FROM BookedRoom br " +
            "WHERE ((br.checkInDate <= :checkOutDate) AND (br.checkOutDate >= :checkInDate))" +
            ")")
    List<Room> findAvailableRoomsByDatesAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
}
