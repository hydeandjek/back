package com.example.demo.qnaapi.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDetailResponseDTO {

    private int commentId;
    private int boardId;

    private String userId;

    private String userName;


    private String content;

    private LocalDateTime regDate;

    private LocalDateTime updateDate;
}
