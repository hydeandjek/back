package com.example.demo.userapi.repository;

import com.example.demo.userapi.entity.SnsLogin;
import com.example.demo.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnsLoginRepository
        extends JpaRepository<SnsLogin, String> {


}
