package com.example.demo.recipeapi.service;

import com.example.demo.recipeapi.repository.RecipeRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Getter
    @Value("${RECIPE-KEY}")
    private String apiKey;


    public String getRecipeList() {
//        List<Recipe> recipeList = recipeRepository.findAll();

        String reqUri = "http://openapi.foodsafetykorea.go.kr/api/"+getApiKey()+"/COOKRCP01/json/1/6";

        // 요청 헤더 설정?

        // 요청 보내기
        RestTemplate template = new RestTemplate();

        // 통신을 보내면서 응답데이터를 리턴
        // param1: 요청 url
        // param2: 요청 메서드
        // param3: 헤더와 요청파라미터정보 엔터티
        // param4: 응답데이터를 받을 객체의 타입 (ex: dto, map)
//        ResponseEntity<KakaoUserDTO> responseEntity
//                = template.exchange(reqUri, HttpMethod.GET, new HttpEntity<>(headers), recipeListResponseDTO.class);

        // 응답 데이터에서 필요한 정보를 가져오기
        Map<String, Objects> responseBody = template.getForObject(reqUri, Map.class);
        log.info("레시피 리스트 요청 응답 데이터: {}", responseBody.toString());

        return responseBody.toString();

//        List<recipeDetailResponseDTO> dtoList = recipeList.stream()
//                .map(recipe -> new recipeDetailResponseDTO(recipe))
//                .collect(Collectors.toList());
//
//        return recipeListResponseDTO.builder()
//                .recipes(dtoList)
//                .build();

    }


    public String getRecipe(String id) {
        String reqUri = "http://openapi.foodsafetykorea.go.kr/api/"+getApiKey()+"/COOKRCP01/json/"+id+"/"+id;

        RestTemplate template = new RestTemplate();

        Map<String, Objects> responseBody = template.getForObject(reqUri, Map.class);
        log.info("레시피 리스트 요청 응답 데이터: {}", responseBody.toString());

        return responseBody.toString();


    }
}
