package com.example.demo.shareapi.repository;

import com.example.demo.shareapi.entity.Share;
import com.example.demo.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Integer> {

    // 카테고리별 글 목록 리턴
//    @Query("SELECT b FROM Board b WHERE b.category = :category")
//    List<Share> findAllByCategory(@Param("category") String category);

    // 사용자가 클릭한 특정한 글 정보 리턴
//    @Query("SELECT s FROM Share s WHERE s.shareId = :boardId")
//    Share findById(
//            @Param("boardId") int boardId);

    // 특정 회원이 작성한 글 목록 리턴
    // SELECT * FROM board b WHERE b.user =: user_id = ?
    @Query("SELECT b FROM Board b WHERE b.user = :user")
    List<Share> findAllByUser(@Param("user") User user);
//
//    // 회원이 작성한 글의 개수를 리턴
//    @Query("SELECT COUNT(*) FROM Board b WHERE b.user = :user")
//    int countByUser(@Param("user") User user);

}
