package com.example.demo.shareapi.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareAdminResponseDTO {
    // 번호, 게시판카테고리, 제목, 날짜, 글쓴이 필요
    private int id;

    private String title;

//    private String category; // 카테고리 받아온거 넣기

    private LocalDateTime regDate;

    private String userId;

    private String imageUrl;

    private boolean approvalFlag;

    private int commentCount; // 댓글수?


}
