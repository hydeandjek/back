package com.example.demo.shareapi.dto.request;

import com.example.demo.shareapi.entity.Images;
import com.example.demo.shareapi.entity.Share;
import com.example.demo.userapi.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareRequestDTO {

    @NotBlank
    @Size(min = 2, max = 30)
    private String title;

    private String content;

//    private List<String> attachmentUrls; // 이미지 url
//    private List<Images> uploadImages;

//    private List<ShareCommentRequestDTO> comments;

    private boolean approvalFlag; // 승인여부 (프론트에서 유즈스테이트 디폴트 설정)


    // dto를 엔티티로 변환
    public Share toEntity(User user){
        return Share.builder()
//                .category(this.category)
                .title(this.title)
                .content(this.content)
//                .uploadImages(this.uploadImages) -->Images객체에서 수정하기
                .approvalFlag(this.approvalFlag)
                .user(user)
                .build();
    }

}
