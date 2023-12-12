package com.example.demo.recipeapi.dto;

import java.util.List;
import lombok.*;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class recipeListResponseDTO {
    private String error; // 에러 발생 시 에러 메세지를 담을 필드
    private List<recipeDetailResponseDTO> recipes;

}
