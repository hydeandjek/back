package com.example.demo.shareapi.dto.response;

import com.example.demo.shareapi.entity.ApprovalStatus;
import com.example.demo.userapi.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareResponseDTO { // 목록 요청 응답 시 사용
    // 번호, 게시판카테고리, 제목, 날짜, 글쓴이 필요
    private int id;

    private String title;

//    private String category; // 카테고리 받아온거 넣기

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime regDate;

    private String userId;

    private String imageUrl;
//    private List<Images> uploadImages;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private String approvalDate;

    private ApprovalStatus approvalFlag;

    private int commentCount; // 댓글수

    private String content;

    private String userName;

}
