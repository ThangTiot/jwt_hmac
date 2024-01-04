package com.example.luvinajwt.repository;

import com.example.luvinajwt.model.Role;
import com.example.luvinajwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByRole(Role role);
}
