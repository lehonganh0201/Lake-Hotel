package com.honganhdev.lakesidehotel.response;
/*
 * @author HongAnh
 * @created 20 / 05 / 2024 - 1:51 PM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class JwtResponse {
    private Integer id;
    private String email;
    private String jwt;
    private String type = "Bearer";
    private List<String> roles;

    public JwtResponse(Integer id, String email, String jwt, List<String> roles) {
        this.id = id;
        this.email = email;
        this.jwt = jwt;
        this.roles = roles;
    }
}
