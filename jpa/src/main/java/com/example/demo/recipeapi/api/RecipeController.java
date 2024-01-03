package com.example.demo.recipeapi.api;

import com.example.demo.auth.TokenUserInfo;
import com.example.demo.recipeapi.dto.LikeRequestDTO;
import com.example.demo.recipeapi.dto.LikeResponseDTO;
import com.example.demo.recipeapi.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;

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
        log.info("/api/menu/recipe GET Recipe List Request");
        Map<String, Objects> responseDTO = recipeService.getRecipeList(pageNum);

        return ResponseEntity.ok().body(responseDTO);
    }

    // 카테고리별 레시피 리스트 요청 처리
    @GetMapping("/{category}/{pageNum}") // 예: 반찬/1페이지
    public ResponseEntity<?> getRecipeList(@PathVariable String category, @PathVariable int pageNum) {
        log.info("/api/menu/recipe/{}/{} - GET Recipe List by Category Request", category, pageNum);
        Map<String, Objects> responseDTO = recipeService.getRecipeListByCategory(category, pageNum);

        return ResponseEntity.ok().body(responseDTO);
    }

    // 레시피 상세보기 요청 처리
    @GetMapping("detail/{name}/{category}/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable String name,
                                       @PathVariable String category,
                                       @PathVariable String id){
        try {
//            URLEncoder.encode(category, "UTF-8");
            log.info("/api/menu/recipe/detail/{}/{}/{} - GET Recipe Detail Request"
                    ,name, category, id);
            Map<String, Object> responseDTO =recipeService.getRecipe(name, category, id);


            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    // 좋아요 기능
    @PostMapping("/like")
    public ResponseEntity<?> insert(@AuthenticationPrincipal TokenUserInfo userInfo,
                                    @RequestBody @Valid LikeRequestDTO likeRequestDTO) {

        log.info("/api/menu/recipe/like - POST request!");
        log.info("likeRequestDTO: {}", likeRequestDTO);
        log.info("찜 누른 유저: {}", userInfo);

        try {

            if(userInfo == null){
                throw new IllegalAccessException("로그인하지 않은 유저입니다!");
            }
            LikeResponseDTO likeResponseDTO
                    = recipeService.insert(likeRequestDTO, userInfo.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(likeResponseDTO);

        } catch (IllegalAccessException e) {
            log.warn("", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage()); // 403
        } catch (RuntimeException e) {
            log.warn("", e);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage()); // 406
        } catch (InterruptedException e) {
            log.warn("", e);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage()); // 502
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/like")
    public ResponseEntity<?> getLikeList (@AuthenticationPrincipal TokenUserInfo userInfo) {
        try {

            if(userInfo == null){
                throw new IllegalAccessException("로그인하지 않은 유저입니다!");
            }

            return ResponseEntity.status(HttpStatus.OK).body(recipeService.getLikeLists(userInfo.getUserId()));

        } catch (IllegalAccessException e) {
            log.warn("", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage()); // 403
        } catch (RuntimeException e) {
            log.warn("", e);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage()); // 406
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @DeleteMapping
//    public ResponseEntity<?> delete(@RequestBody @Valid LikeRequestDTO likeRequestDTO) {
//        try {
//            recipeService.delete(likeRequestDTO);
//            return ResponseEntity.status(HttpStatus.OK).body("SUCCESS");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
//        }
//    }






}
