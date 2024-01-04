package com.example.demo.userapi.repository;

import com.example.demo.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findById(String id);

    @Query("SELECT s.userAddress FROM User s WHERE s.id = :id ")
    String findMyAddr(@Param("id") String id);
}
