package com.example.demo.furnitureapi.repository;

import com.example.demo.furnitureapi.entity.Furniture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FurnitureRepository extends JpaRepository<Furniture, Integer> {
}
