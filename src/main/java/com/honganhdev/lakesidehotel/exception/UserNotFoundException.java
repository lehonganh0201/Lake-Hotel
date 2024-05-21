package com.honganhdev.lakesidehotel.exception;
/*
 * @author HongAnh
 * @created 19 / 05 / 2024 - 8:11 PM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
