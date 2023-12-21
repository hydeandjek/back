package com.example.demo.qnaapi.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentListResponseDTO {

    private String error; //에러 발생 시 에러 메세지를 담을 필드
    private List<BoardDetailResponseDTO> boards; // 글 하나의 정보
}
