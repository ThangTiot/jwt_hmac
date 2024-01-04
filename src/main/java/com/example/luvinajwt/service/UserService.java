package com.example.luvinajwt.service;

import com.example.luvinajwt.model.User;

public interface UserService {
    User getCurrentUser();

    User getUserByUserName(String userName);

    User getUserFromToken(String token);
}
