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
public class ShareCommentResponseDTO {
    // 글쓴이, 내용, 날짜 필요
//    private int commentId;

    private String content;

    private LocalDateTime regDate; // 등록날짜 또는 수정날짜 (가장 최근 날짜)

    private String userId;

    private int boardId;


}
