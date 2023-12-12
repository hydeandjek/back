package com.example.demo.recipeapi.dto;

import com.example.demo.recipeapi.entity.Recipe;
import lombok.*;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class recipeDetailResponseDTO {
    private int recipeId;
    private String name;
    private String recipeMethod;
    private String recipeCategory;
    private String imgSrc;


    public recipeDetailResponseDTO(Recipe recipe){
        this.recipeId = recipe.getRecipeId();
        this.name = recipe.getName();
        this.recipeMethod = recipe.getWay();
        this.recipeCategory = recipe.getType();
        this.imgSrc = recipe.getImgSrc();
    }
}
