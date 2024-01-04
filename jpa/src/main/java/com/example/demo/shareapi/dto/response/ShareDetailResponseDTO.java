package com.example.demo.shareapi.dto.response;

import com.example.demo.shareapi.entity.ApprovalStatus;
import com.example.demo.shareapi.entity.Images;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareDetailResponseDTO { // 상세 보기 요청 응답 시 사용
    private int id;

    private String title;

    private String content;

//    private List<String> attachmentUrls; // 이미지 url

    private List<Images> uploadImages;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime regDate;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private String approvalDate;

    private String userId;

    private String userName;

    private List<ShareCommentResponseDTO> comments; // 등록 요청 응답 시에는 달린 댓글이 없을 것, 상세 요청 응답 시에는 존재할 것


    private ApprovalStatus approvalFlag; // 승인여부 (프론트에서 유즈스테이트 디폴트 설정)


}
