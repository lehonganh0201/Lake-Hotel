package com.honganhdev.lakesidehotel.exception;
/*
 * @author HongAnh
 * @created 30 / 04 / 2024 - 5:21 PM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }
}
