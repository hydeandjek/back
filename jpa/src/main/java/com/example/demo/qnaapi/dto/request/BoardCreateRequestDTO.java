package com.example.demo.qnaapi.dto.request;

import com.example.demo.qnaapi.entity.QuestionBoard;
import com.example.demo.userapi.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardCreateRequestDTO {


    @NotBlank
    @Size(min = 2, max = 30)
    private String title;

    @NotBlank
    @Size(min = 2, max = 85)
    private String content;

    // dto를 엔터티로 변환하는 메서드
    public QuestionBoard toEntity(User user){
        return QuestionBoard.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .build();
    }


}
