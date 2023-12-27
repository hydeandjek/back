package com.example.demo.publicapi.repository;

import com.example.demo.publicapi.entity.Public;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicRepository extends JpaRepository<Public, Integer> {
}
