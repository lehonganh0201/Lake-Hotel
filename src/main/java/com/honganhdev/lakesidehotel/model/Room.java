package com.honganhdev.lakesidehotel.model;
/*
 * @author HongAnh
 * @created 25 / 04 / 2024 - 6:05 PM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_Type")
    private String roomType;

    @Column(name = "room_Price")
    private BigDecimal roomPrice;

    @Column(name = "is_Booked")
    private boolean isBooked = false;

    @Lob
    private Blob photo;

    @OneToMany(mappedBy = "room",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookedRoom> bookings;

    public Room() {
        bookings = new ArrayList<>();
    }

    public void addBooking(BookedRoom bookedRoom){
        if(bookings == null){
            bookings = new ArrayList<>();
        }

        bookings.add(bookedRoom);
        bookedRoom.setRoom(this);
        isBooked = true;
        String bookingCode = RandomStringUtils.randomNumeric(10);
        bookedRoom.setBookingConfirmationCode(bookingCode);
    }
}
