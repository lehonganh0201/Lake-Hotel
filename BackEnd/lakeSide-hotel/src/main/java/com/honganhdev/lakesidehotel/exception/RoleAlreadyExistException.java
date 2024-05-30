package com.honganhdev.lakesidehotel.exception;
/*
 * @author HongAnh
 * @created 19 / 05 / 2024 - 9:20 PM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

public class RoleAlreadyExistException extends RuntimeException {

    public RoleAlreadyExistException(String message) {
        super(message);
    }
}
