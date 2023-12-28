package com.example.demo.mealkitapi.repository;

import com.example.demo.mealkitapi.entity.MealKit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MealKitRepository extends JpaRepository<MealKit, Integer> {

}
