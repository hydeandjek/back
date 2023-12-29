package com.example.demo.recipeapi.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
public class LikeResponseDTO {
    @NotBlank
    private String userId;
    private String recipeName;
    private boolean done;

    public LikeResponseDTO(String userId, String recipeName, boolean done) {
        this.userId = userId;
        this.recipeName = recipeName;
        this.done = done;
    }
}
