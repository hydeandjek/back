package com.example.demo.shareapi.dto.response;

import com.example.demo.shareapi.entity.Share;
import com.example.demo.userapi.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    private int commentId;

    private String content;


    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime regDate; // 등록날짜 또는 수정날짜 (가장 최근 날짜)

    private String userId;

    private int boardId;

    private String userName;

//    public ShareCommentResponseDTO toEntity(User user, Share share){
//        this.userId = user.getId();
//        this.boardId = share.getShareId();
//    }


}
