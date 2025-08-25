package com.thanhthbm.restaurant.domain.response;

import com.thanhthbm.restaurant.util.constant.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String username;
    private String email;
    private Gender gender;
    private String address;
    private Instant createdAt;

}
