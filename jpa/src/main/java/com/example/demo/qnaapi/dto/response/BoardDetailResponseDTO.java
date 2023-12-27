package com.example.demo.qnaapi.dto.response;


import com.example.demo.qnaapi.entity.QuestionBoard;
import com.example.demo.userapi.entity.User;
import lombok.*;
import net.bytebuddy.implementation.bind.annotation.This;

import java.time.LocalDateTime;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDetailResponseDTO {

    private int rowNumber;

    private int boardId;

    private String userId;

    private String userName;

    private String title;

    private String content;

    private LocalDateTime regDate;

    private LocalDateTime updateDate;

//    public QuestionBoard entity(int id){
//        BoardDetailResponseDTO.builder()
//                .title(this.getTitle())
//                .content(this.getContent())
//                .regDate(this.getRegDate())
//                .regDate(this.getUpdateDate())
//                .boardId(id)
//                .userId(this.userId)
//                .build();
//    }




}
