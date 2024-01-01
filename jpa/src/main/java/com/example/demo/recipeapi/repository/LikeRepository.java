package com.example.demo.recipeapi.repository;

import com.example.demo.boardapi.entity.Board;
import com.example.demo.recipeapi.entity.Like;
import com.example.demo.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    // 레시피이름으로 찜 테이블 조회
    @Query("SELECT l FROM Like l WHERE l.recipeName = :recipeName")
    Optional<Like> findByRecipeName(@Param("recipeName") String recipeName);

//    Optional<Like> findByRecipeName(String recipeName);

    // 사용자가 클릭한 특정한 레시피 정보 리턴
//    @Query("SELECT b FROM Board b WHERE b.category = :category AND b.boardId = :boardId")
//    Board findByCategoryAndId(
//            @Param("category") String category,
//            @Param("boardId") int boardId);

    // 특정 회원이 찜한 레시피 목록 리턴
    // SELECT * FROM board b WHERE b.user =: user_id = ?
    @Query("SELECT l FROM Like l WHERE l.user = :user")
    List<Like> findAllByUser(@Param("user") User user);

    // 회원이 찜한 레시피 개수를 리턴
    @Query("SELECT COUNT(*) FROM Like l WHERE l.user = :user")
    int countByUser(@Param("user") User user);
}
