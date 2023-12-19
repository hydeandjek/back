package com.example.demo.recipeapi.api;

import com.example.demo.recipeapi.dto.recipeListResponseDTO;
import com.example.demo.recipeapi.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/menu/recipe")
@CrossOrigin
public class RecipeController {

    private final RecipeService recipeService;


    // 전체 레시피 리스트 요청 처리
    @GetMapping("/total/{pageNum}")
    public ResponseEntity<?> getRecipeList(@PathVariable int pageNum){
        log.info("/api/recipe GET Recipe List Request");
        String responseDTO = recipeService.getRecipeList(pageNum);

        return ResponseEntity.ok().body(responseDTO);
    }

    // 카테고리별 레시피 리스트 요청 처리
    @GetMapping("/{category}")
    public ResponseEntity<?> getRecipeList(@PathVariable String category){
        log.info("/api/recipe/{} - GET Recipe List Request", category);
//        String responseDTO = recipeService.getRecipeList();

//        return ResponseEntity.ok().body(responseDTO);
        return null;
    }

    // 레시피 상세보기 요청 처리
    @GetMapping("/{category}/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable String category, @PathVariable String id){
        log.info("/api/recipe/{}/{} - GET Recipe Detail Request", category, id);
        String responseDTO = recipeService.getRecipe(id);


        return ResponseEntity.ok().body(responseDTO);

    }






}
