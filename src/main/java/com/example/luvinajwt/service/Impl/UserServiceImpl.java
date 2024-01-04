package com.example.luvinajwt.service.Impl;

import com.example.luvinajwt.jwt.JwtTokenProvider;
import com.example.luvinajwt.model.User;
import com.example.luvinajwt.repository.UserRepository;
import com.example.luvinajwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public User getCurrentUser() {
        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        return (User) securityContextHolder.getAuthentication().getPrincipal();
    }

    @Override
    public User getUserByUserName(String userName) {
        return userRepository.findByEmail(userName);
    }

    @Override
    public User getUserFromToken(String token) {
        String userName = jwtTokenProvider.getUserNameFromToken(token);
        return getUserByUserName(userName);
    }
}
