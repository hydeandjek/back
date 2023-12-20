package com.example.demo.recipeapi.service;


import com.example.demo.recipeapi.repository.RecipeRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RecipeService {

    private final RecipeRepository recipeRepository;

    EntityManager em; // JPA 관리 핵심 객체

    JPAQueryFactory factory; // QueryDSL로 쿼리문을 작성하기 위한 핵심 객체

    @Getter
    @Value("${RECIPE-KEY}")
    private String apiKey;


    public Map<String, Objects> getRecipeList(int pageNum) {
//        factory = new JPAQueryFactory(em);
//        List<Recipe> recipeList = factory.selectFrom()
        String reqUri;
        if(pageNum == 1){ // 1
            reqUri = "http://openapi.foodsafetykorea.go.kr/api/"+getApiKey()+"/COOKRCP01/json/"
                    +pageNum+"/"+ pageNum*6;
        }else { // 2~
            reqUri = "http://openapi.foodsafetykorea.go.kr/api/"+getApiKey()+"/COOKRCP01/json/"
                    +(((pageNum-1)*6)+1)+"/"+ pageNum*6;
        }
//        HttpURLConnection urlConnection = (HttpURLConnection) reqUri.open

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
        log.info("레시피 리스트 요청 응답 데이터: {}", responseBody);

        return responseBody;

//        List<recipeDetailResponseDTO> dtoList = recipeList.stream()
//                .map(recipe -> new recipeDetailResponseDTO(recipe))
//                .collect(Collectors.toList());
//
//        return recipeListResponseDTO.builder()
//                .recipes(dtoList)
//                .build();

    }


    public Map<String, Objects> getRecipeListByCategory(String category, int pageNum) {
        String reqUri;
        if(pageNum == 1){ // 1
            reqUri = "http://openapi.foodsafetykorea.go.kr/api/"+getApiKey()+"/COOKRCP01/json/"
                    +pageNum+"/"+ pageNum*6+"/RCP_PAT2="+category;
        }else { // 2~
            reqUri = "http://openapi.foodsafetykorea.go.kr/api/"+getApiKey()+"/COOKRCP01/json/"
                    +(((pageNum-1)*6)+1)+"/"+ pageNum*6+"/RCP_PAT2="+category;
        }

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
        log.info("레시피 리스트 요청 응답 데이터: {}", responseBody);

        return responseBody;
    }

    public String getRecipe(String category, String id) throws ParseException {

        String reqUri = "http://openapi.foodsafetykorea.go.kr/api/"+getApiKey()+"/COOKRCP01/json/"+id+"/"+id;

        RestTemplate template = new RestTemplate();

        String jsonString = template.getForObject(reqUri, String.class); // 스트링으로 받아야 그대로 받음.
        log.info("레시피 리스트 요청 응답 데이터: {}", jsonString);

        // api요청으로 받아온 json데이터의 요리 카테고리 추출
        JSONParser jsonParser = new JSONParser();
//        //JSON데이터를 넣어 JSON Object 로 만들어 준다.
//         JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody
//                                                                .toString()
//                                                                .replaceAll("●", ""));
////                                                                .replaceAll("\\uFEFF", ""));
        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString); // 파싱


//        JSONObject cookrcp01 = (JSONObject) jsonObject.get("COOKRCP01"); // 객체 추출
        JSONObject cookrcp01 = (JSONObject) jsonObject.get("COOKRCP01");
        JSONArray recipe = (JSONArray) cookrcp01.get("row"); // 배열 추출

        log.info("배열: {}", recipe);

         JSONObject recipeObj = (JSONObject) recipe.get(0); // 배열 안 객체 추출
        String rcpCategory = (String) recipeObj.get("RCP_PAT2"); // 객체 안의 키의 값 추출

        // 매개변수로 받은 카테고리 != api요청에 따라 받은 요리의 카테고리
        if(!category.equals(rcpCategory)){
            throw new IllegalArgumentException("해당 카테고리와 아이디에 맞는 레시피를 가져올 수 없습니다!");
        }

        return jsonString;

    }



}
