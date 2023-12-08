package com.example.demo.recipeapi.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter @Setter
@ToString @EqualsAndHashCode(of = "recipeId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String recipeId;

    private String recipeCategory;

    private String imgSrc;

    private String name;



}
