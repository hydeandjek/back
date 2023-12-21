package com.example.demo.qnaapi.repository;

import com.example.demo.qnaapi.entity.QuestionBoard;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QnaBoardRepository
        extends JpaRepository<QuestionBoard, Integer> {

    // 게시물 리스트

    @Query("SELECT q FROM QuestionBoard q ORDER BY q.boardId ASC")
    @NotNull
    List<QuestionBoard> findAll();

    // 사용자가 클릭한 게시물 상세보기
    @Query("SELECT q FROM QuestionBoard q WHERE q.boardId = :id")
    QuestionBoard findById(@Param("id") int id);









    @Query("SELECT q FROM QuestionBoard q WHERE q.boardId = :id")
    QuestionBoard findAllById(@Param("id") int id);

}
