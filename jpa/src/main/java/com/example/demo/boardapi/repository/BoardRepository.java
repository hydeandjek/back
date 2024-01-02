package com.example.demo.boardapi.repository;

import com.example.demo.boardapi.entity.Board;
import com.example.demo.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    // 카테고리별 글 목록 리턴
    @Query("SELECT b FROM Board b WHERE b.category = :category ORDER BY regDate DESC")
    List<Board> findAllByCategory(@Param("category") String category);

    // 사용자가 클릭한 특정한 글 정보 리턴
    @Query("SELECT b FROM Board b WHERE b.category = :category AND b.boardId = :boardId")
    Board findByCategoryAndId(
            @Param("category") String category,
            @Param("boardId") int boardId);

    @Query("SELECT b FROM Board b WHERE b.boardId = :boardId")
    Board findByBoardId(
            @Param("boardId") int boardId);

    // 특정 회원이 작성한 글 목록 리턴
    // SELECT * FROM board b WHERE b.user =: user_id = ?
    @Query("SELECT b FROM Board b WHERE b.user = :user")
    List<Board> findAllByUser(@Param("user") User user);
//
//    // 회원이 작성한 글의 개수를 리턴
//    @Query("SELECT COUNT(*) FROM Board b WHERE b.user = :user")
//    int countByUser(@Param("user") User user);

}
