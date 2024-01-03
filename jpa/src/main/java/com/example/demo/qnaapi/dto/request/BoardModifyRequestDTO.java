package com.example.demo.qnaapi.dto.request;

import com.example.demo.qnaapi.entity.QuestionBoard;
import com.example.demo.userapi.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardModifyRequestDTO {

    @NotBlank
    @Size(min = 2, max = 50)
    private String title;

    @NotBlank
    @Size(min = 2, max = 200)
    private String content;





    // dto를 엔터티로 변환하는 메서드
    public QuestionBoard toEntity(User user, int id, LocalDateTime regDate){
        return QuestionBoard.builder()
                .boardId(id)
                .title(this.title)
                .content(this.content)
                .user(user)
                .regDate(regDate)
                .updateDate(LocalDateTime.now())
                .build();
    }


}
