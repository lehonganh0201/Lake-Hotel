package com.honganhdev.lakesidehotel.exception;
/*
 * @author HongAnh
 * @created 30 / 04 / 2024 - 2:57 PM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

public class NotFoundRoomException extends RuntimeException{

    public NotFoundRoomException(String message) {
        super(message);
    }
}
