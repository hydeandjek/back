package com.example.demo.qnaapi.repository;

import com.example.demo.qnaapi.entity.QuestionComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QnaCommentRepository
        extends JpaRepository<QuestionComment, Integer> {

    // 게시물 리스트
    List<QuestionComment> findAll();


    // 사용자가 클릭한 게시물 상세보기
    @Query("SELECT q FROM QuestionComment q WHERE q.commentId = :id")
    QuestionComment findById(@Param("id") int id);



    @Query("SELECT q FROM QuestionComment q WHERE q.board.boardId = :id")
    QuestionComment findAllById(@Param("id") int id);



    @Query("SELECT q FROM QuestionComment q WHERE q.board.boardId = :boardId")
    List<QuestionComment> findAllByBoardId(@Param("boardId") int boardId);

    @Query("SELECT q FROM QuestionComment q WHERE q.board.boardId = :boardId ORDER BY q.commentId ASC")
    List<QuestionComment> findboard(@Param("boardId") int boardId);


}
