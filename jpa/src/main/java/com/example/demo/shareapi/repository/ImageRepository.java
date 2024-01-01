package com.example.demo.shareapi.repository;

import com.example.demo.shareapi.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageRepository extends JpaRepository<Images, Integer> {

    // share_id에 따른 모든 이미지 조회 -->인덱스0의 경로만 목록에서 보여줄 것.
    @Query("SELECT i FROM Images i WHERE i.share.shareId = :boardId")
    List<Images> findAllByBoardId(@Param("boardId") int boardId);

    // share_id에 따른 모든 이미지 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM Images i WHERE i.share.shareId = :shareId")
    void deleteByBoardId(@Param("shareId") int shareId);

}
