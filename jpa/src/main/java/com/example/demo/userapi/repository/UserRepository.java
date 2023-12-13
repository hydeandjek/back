package com.example.demo.userapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.userapi.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

}
