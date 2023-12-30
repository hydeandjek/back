package com.example.demo.shareapi.dto;

import com.example.demo.shareapi.entity.Share;
import com.example.demo.shareapi.entity.ShareComment;
import com.example.demo.userapi.entity.User;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareCommentRequestDTO {

    private String content;


    // dto를 엔티티로 변환
    public ShareComment toEntity(User user, Share share){
        return ShareComment.builder()
                .content(this.content)
                .user(user)
                .share(share)
                .build();
    }

}
