package com.example.demo.applianceapi.repository;

import com.example.demo.applianceapi.entity.Appliance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplianceRepository extends JpaRepository<Appliance, Integer> {
}
