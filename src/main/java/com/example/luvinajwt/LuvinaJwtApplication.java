package com.example.luvinajwt;

import com.example.luvinajwt.model.Role;
import com.example.luvinajwt.model.User;
import com.example.luvinajwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LuvinaJwtApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(LuvinaJwtApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByRole(Role.ADMIN) == null) {
            User admin = new User();
            admin.setName("ADMIN");
            admin.setEmail("admin@gmail.com");
            admin.setPass(passwordEncoder.encode("12345"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }
        if (userRepository.findByRole(Role.USER) == null) {
            User user = new User();
            user.setName("USER");
            user.setEmail("user@gmail.com");
            user.setPass(passwordEncoder.encode("12345"));
            user.setRole(Role.USER);
            userRepository.save(user);
        }
    }
}
