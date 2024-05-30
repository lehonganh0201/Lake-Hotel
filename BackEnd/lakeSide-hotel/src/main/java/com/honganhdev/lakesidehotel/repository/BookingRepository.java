package com.honganhdev.lakesidehotel.repository;
/*
 * @author HongAnh
 * @created 25 / 04 / 2024 - 6:09 PM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

import com.honganhdev.lakesidehotel.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookedRoom,Long> {
    @Query(value = "SELECT br FROM BookedRoom br WHERE br.room.id = :id",nativeQuery = true)
    List<BookedRoom> findAllByRoomId(Long id);

    Optional<BookedRoom> findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> findAllByGuestEmail(String email);
}
