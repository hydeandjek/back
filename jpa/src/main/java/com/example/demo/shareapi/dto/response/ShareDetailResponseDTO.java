package com.example.demo.shareapi.dto.response;

import com.example.demo.shareapi.entity.Images;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareDetailResponseDTO {
    // 번호, 게시판카테고리, 제목, 내용, 날짜, 글쓴이 필요
    private int id;

    private String title;

    private String content;

//    private List<String> attachmentUrls; // 이미지 url

    private List<Images> uploadImages;

    private LocalDateTime regDate;

    private LocalDateTime approvalDate;

    private String userId;

    private List<ShareCommentResponseDTO> comments; // 등록 요청 응답 시에는 달린 댓글이 없을 것, 상세 요청 응답 시에는 존재할 것


    private boolean approvalFlag; // 승인여부 (프론트에서 유즈스테이트 디폴트 설정)


}
