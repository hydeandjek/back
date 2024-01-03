package com.example.demo.recipeapi.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class LikeResponseDTO {
    @NotBlank
    private String userId;
    private String recipeName;
    private boolean done;
    private List<String> likedRecipeList; // 찜한 레시피명 리스트
    private int likeCount; // 찜한 개수


//    public LikeResponseDTO(String userId, String recipeName, boolean done) {
//        this.userId = userId;
//        this.recipeName = recipeName;
//        this.done = done;
//
//    }
}
