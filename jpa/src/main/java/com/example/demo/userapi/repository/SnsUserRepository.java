package com.example.demo.userapi.repository;

import com.example.demo.userapi.entity.SnsLogin;
import com.example.demo.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SnsUserRepository
        extends JpaRepository<SnsLogin, String> {

    Optional<SnsLogin> findByEmail(String email);

    boolean existsById(String id);


}
