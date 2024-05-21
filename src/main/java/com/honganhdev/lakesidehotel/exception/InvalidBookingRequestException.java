package com.honganhdev.lakesidehotel.exception;
/*
 * @author HongAnh
 * @created 06 / 05 / 2024 - 5:40 AM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

public class InvalidBookingRequestException extends RuntimeException{

    public InvalidBookingRequestException(String message) {
        super(message);
    }
}
