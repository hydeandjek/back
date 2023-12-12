package com.example.demo.recipeapi.repository;

import com.example.demo.recipeapi.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {



}
