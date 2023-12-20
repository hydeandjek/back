package com.example.demo.mapapi.repository;

import com.example.demo.mapapi.entity.AdministrativeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdministrativeCodeRepository extends JpaRepository<AdministrativeCode,Integer> {

    @Query("SELECT d.dong FROM AdministrativeCode d WHERE d.gu = :gu")
    List<String> getDongList(@Param("gu") String gu);

}
