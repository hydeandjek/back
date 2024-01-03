package com.example.demo.shareapi.dto.response;

import com.example.demo.userapi.entity.User;
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

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime regDate;

    private String userId;

    private String imageUrl;
//    private List<Images> uploadImages;

    private String approvalDate;

    private boolean approvalFlag;

    private int commentCount; // 댓글수

    private String content;

    private String userName;

}
