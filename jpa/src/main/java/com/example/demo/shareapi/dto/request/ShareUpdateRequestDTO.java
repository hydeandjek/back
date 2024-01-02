package com.example.demo.shareapi.dto.request;

import com.example.demo.shareapi.entity.Share;
import com.example.demo.userapi.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareUpdateRequestDTO {

    @NotBlank
    @Size(min = 2, max = 30)
    private String title;

    private String content;

//    private String category; // 사용자가 선택한 카테고리

    // dto를 엔티티로 변환
    public Share toEntity(User user){
        return Share.builder()
//                .category(this.category)
                .title(this.title)
                .content(this.content)
                .user(user)
                .build();
    }


}
