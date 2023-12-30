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

    private boolean done;

    public LikeRequestDTO(String recipeName, boolean done) {
//        this.userId = userId;
        this.recipeName = recipeName;
        this.done = done;
    }

}
