package com.example.luvinajwt.controller;

import com.example.luvinajwt.jwt.JwtTokenProvider;
import com.example.luvinajwt.model.AuthenRespone;
import com.example.luvinajwt.model.User;
import com.example.luvinajwt.service.AuthenService;
import com.example.luvinajwt.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenService authenService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthenRespone> login(@RequestBody User user) {
        // Xác thực user có khớp vs trong db không.
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPass()));
        User userDetail = userService.getUserByUserName(user.getUsername());
        AuthenRespone authenRespone = authenService.generateToken(userDetail);
        ResponseCookie jwtRefreshCookie = authenService.setCookieRefreshToken(authenRespone.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(authenRespone);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthenRespone> refreshToken(HttpServletRequest request) {
        String refreshToken = authenService.getRefreshTokenFromCookie(request);
        if (StringUtils.hasText(refreshToken) && jwtTokenProvider.validateToken(refreshToken)) {
            User userDetail = userService.getUserFromToken(refreshToken);
            AuthenRespone authenRespone = authenService.generateToken(userDetail);
            ResponseCookie jwtRefreshCookie = authenService.setCookieRefreshToken(authenRespone.getRefreshToken());
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                    .body(authenRespone);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
