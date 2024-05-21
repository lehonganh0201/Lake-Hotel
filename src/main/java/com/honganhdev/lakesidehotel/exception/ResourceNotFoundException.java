package com.honganhdev.lakesidehotel.exception;
/*
 * @author HongAnh
 * @created 29 / 04 / 2024 - 3:45 PM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
