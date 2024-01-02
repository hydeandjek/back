package com.example.demo.shareapi.repository;

import com.example.demo.shareapi.entity.Share;
import com.example.demo.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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
    @Query("SELECT s FROM Share s WHERE s.user = :user")
    List<Share> findAllByUser(@Param("user") User user);

    // 승인여부가 false이고 특정회원이 작성한 글 목록 리턴
    @Query("SELECT s FROM Share s WHERE s.approvalFlag = false AND s.user = :user ")
    List<Share> findAllByUserYetApprovedShares(@Param("user") User user);

    // 회원이 작성한 미승인된 글의 개수를 리턴
    @Query("SELECT COUNT(*) FROM Share s WHERE s.approvalFlag = false AND s.user = :user")
    int countByUserYetApproved(@Param("user") User user);

    // 회원이 작성한 승인된 글의 개수를 리턴
    @Query("SELECT COUNT(*) FROM Share s WHERE s.approvalFlag = true AND s.user = :user")
    int countByUserApproved(@Param("user") User user);

    // 승인여부가 false인 글 목록 리턴
    @Query("SELECT s FROM Share s WHERE s.approvalFlag = false")
    List<Share> findYetApprovedShares();

    // 승인여부가 false이고 id에 따른 글 리턴
    @Query("SELECT s FROM Share s WHERE s.approvalFlag = false AND s.shareId = :shareId ")
    Optional<Share> findByIdYetApprovedShares(@Param("shareId") int shareId);



}
