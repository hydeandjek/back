package com.example.demo.qnaapi.dto.request;

import com.example.demo.qnaapi.entity.QuestionBoard;
import com.example.demo.qnaapi.entity.QuestionComment;
import com.example.demo.userapi.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateRequestDTO {



    @NotBlank
    @Size(min = 2, max = 200)
    private String content;





    // dto를 엔터티로 변환하는 메서드

    public QuestionComment toEntity(User user, QuestionBoard byId) {
        return QuestionComment.builder()
                .content(this.content)
                .board(byId)
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
    }
}
