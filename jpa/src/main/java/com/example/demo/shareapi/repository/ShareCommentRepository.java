package com.example.demo.shareapi.repository;

import com.example.demo.shareapi.entity.ShareComment;
import com.example.demo.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShareCommentRepository extends JpaRepository<ShareComment, Integer> {

    // 특정 게시글의 댓글 목록 조회
    @Query("SELECT c FROM ShareComment c WHERE c.share.shareId = :boardId")
    List<ShareComment> findAllByBoardId(@Param("boardId") int boardId);

    // 특정 게시글의 댓글 개수 조회
    @Query("SELECT COUNT(*) FROM ShareComment c WHERE c.share.shareId = :boardId")
    int countByBoard(@Param("boardId") int boardId);

    // 특정 회원이 작성한 댓글 목록 리턴
    // SELECT * FROM board b WHERE b.user =: user_id = ?
    @Query("SELECT c FROM ShareComment c WHERE c.user = :user")
    List<ShareComment> findAllByUser(@Param("user") User user);

    // 회원이 작성한 댓글의 개수를 리턴
    @Query("SELECT COUNT(*) FROM ShareComment c WHERE c.user = :user")
    int countByUser(@Param("user") User user);

}
