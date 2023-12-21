package com.example.demo.qnaapi.dto.request;

import com.example.demo.qnaapi.entity.QuestionComment;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentModifyRequestDTO {

    private String content;

    public QuestionComment toEntity(QuestionComment bycommentId) {
        return QuestionComment.builder()
                .user(bycommentId.getUser())
                .commentId(bycommentId.getCommentId())
                .board(bycommentId.getBoard())
                .updateDate(LocalDateTime.now())
                .content(this.content)
                .regDate(bycommentId.getRegDate())
                .build();
    }


    // dto를 엔터티로 변환하는 메서드



}
