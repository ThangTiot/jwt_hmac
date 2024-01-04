package com.example.luvinajwt.service;

import com.example.luvinajwt.model.AuthenRespone;
import com.example.luvinajwt.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

public interface AuthenService {

    AuthenRespone generateToken(User user);

    ResponseCookie setCookieRefreshToken(String refreshToken);

    String getRefreshTokenFromCookie(HttpServletRequest request);

}
