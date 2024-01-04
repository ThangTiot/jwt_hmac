package com.example.luvinajwt.service.Impl;

import com.example.luvinajwt.jwt.JwtTokenProvider;
import com.example.luvinajwt.model.AuthenRespone;
import com.example.luvinajwt.model.User;
import com.example.luvinajwt.service.AuthenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

@Service
@RequiredArgsConstructor
public class AuthenServiceImpl implements AuthenService {

    private final JwtTokenProvider jwtTokenProvider;

    private final String JWT_REFRESH_TOKEN = "JWT_REFRESH_COOKIE";

    private final Long REFRESH_JWT_EXPIRATION = 604800L;

    @Override
    public AuthenRespone generateToken(User user) {
        AuthenRespone authenRespone = new AuthenRespone();
        authenRespone.setToken(jwtTokenProvider.generateToken(user));
        authenRespone.setRefreshToken(jwtTokenProvider.generateRefreshToken(user));
        return authenRespone;
    }

    @Override
    public ResponseCookie setCookieRefreshToken(String refreshToken) {
        // httpOnly cho phép chỉ có thể lấy được cookie ở phía server, tránh tấn công từ FE bằng cách thêm đoạn mã js để lấy ra được cookie
        return ResponseCookie.from(JWT_REFRESH_TOKEN, refreshToken).maxAge(REFRESH_JWT_EXPIRATION).httpOnly(true).build();
    }

    @Override
    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, JWT_REFRESH_TOKEN);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }
}
