package com.example.demo.mapapi.repository;

import com.example.demo.mapapi.entity.Cctv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CctvRepository extends JpaRepository<Cctv, Integer> {

    @Query("SELECT c FROM Cctv c WHERE c.gu = :gu AND c.dong LIKE %:dong%")
    List<Cctv> findAllByGuAndDong(@Param("gu") String gu, @Param("dong") String dong);

}
