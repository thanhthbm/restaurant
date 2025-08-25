package com.thanhthbm.restaurant.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thanhthbm.restaurant.domain.Role;
import lombok.*;

@Getter
@Setter
public class ResLoginDTO {

    @JsonProperty("access_token")
    private String accessToken;

    private UserLogin user;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserLogin{
        private long id;
        private String name;
        private String username;
        private String email;
        private Role role;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserInsideToken{
        private long id;
        private String email;
        private String username;
        private String name;
    }


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserGetAccount{
        private UserLogin user;
    }
}
