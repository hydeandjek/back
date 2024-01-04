package com.example.demo.boardapi.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPostResponseDTO {
    private int rowNumber;

    private int boardId;

    private String category;

    private String userId;

    private String userName;

    private String title;

    private String content;

    private LocalDateTime regDate;


}
