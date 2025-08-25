package com.thanhthbm.restaurant.controller;

import com.thanhthbm.restaurant.domain.User;
import com.thanhthbm.restaurant.domain.request.ReqLoginDTO;
import com.thanhthbm.restaurant.domain.response.ResLoginDTO;
import com.thanhthbm.restaurant.service.UserService;
import com.thanhthbm.restaurant.util.SecurityUtil;
import com.thanhthbm.restaurant.util.annotation.ApiMessage;
import com.thanhthbm.restaurant.util.exception.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    private final SecurityUtil securityUtil;
    private final UserService userService;
    private PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, UserService userService, PasswordEncoder passwordEncoder,  SecurityUtil securityUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDTO){

        //load username/password into Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(),
                loginDTO.getPassword()
        );

        //authenticate user by implementing loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        //load authentication into Spring context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = new ResLoginDTO();
        User currentUserInDB = userService.getUserByUsername(loginDTO.getUsername());

        if (currentUserInDB != null){
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    currentUserInDB.getId(),
                    currentUserInDB.getName(),
                    currentUserInDB.getUsername(),
                    currentUserInDB.getEmail(),
                    currentUserInDB.getRole()
            );
        }

        //create accessToken
        String access_token = this.securityUtil.createAccessToken(authentication.getName(), res);


        //create refresh token
        String refresh_token = this.securityUtil.createRefreshToken(authentication.getName(), res);

        //update user
        this.userService.updateUserToken(refresh_token, loginDTO.getUsername());

        //set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @PostMapping("/auth/logout")
    @ApiMessage("Logout User")
    public ResponseEntity<Void> logout() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        if (email.equals("")) {
            throw new IdInvalidException("Access Token không hợp lệ");
        }

        // update refresh token = null
        this.userService.updateUserToken(null, email);

        // remove refresh token cookie
        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                .body(null);
    }

}
