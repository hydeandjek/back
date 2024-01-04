package com.example.demo.recipeapi.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
public class LikeRequestDTO {

//    @NotBlank
//    private String userId;
    @NotBlank
    private String recipeName;

    private String recipeImg;

    private boolean done;



    public LikeRequestDTO(String recipeName, String recipeImg, boolean done) {
//        this.userId = userId;
        this.recipeName = recipeName;
        this.recipeImg = recipeImg;
        this.done = done;
    }

}
