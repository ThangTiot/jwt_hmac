package com.example.luvinajwt.model;

import lombok.Data;

@Data
public class AuthenRespone {

    private String token;

    private String refreshToken;
}
