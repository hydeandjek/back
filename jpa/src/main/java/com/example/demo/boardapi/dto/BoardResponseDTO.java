package com.example.demo.boardapi.dto;

import com.example.demo.boardapi.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResponseDTO {
    // 번호, 게시판카테고리, 제목, 날짜, 글쓴이 필요
    private int id;

    private String title;

    private String category; // 카테고리 받아온거 넣기

    private LocalDateTime regDate;

    private String userId;

    private String userName;

    private int rowNum;

//    private int count; // 댓글수?


}
