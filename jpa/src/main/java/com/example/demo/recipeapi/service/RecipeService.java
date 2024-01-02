package com.example.demo.recipeapi.service;


import com.example.demo.recipeapi.dto.LikeRequestDTO;
import com.example.demo.recipeapi.dto.LikeResponseDTO;
import com.example.demo.recipeapi.entity.Like;
import com.example.demo.recipeapi.entity.QLike;
import com.example.demo.recipeapi.repository.LikeRepository;
import com.example.demo.recipeapi.repository.RecipeRepository;
import com.example.demo.userapi.entity.User;
import com.example.demo.userapi.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.example.demo.recipeapi.entity.QLike.like;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

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
                    +pageNum+"/"+ pageNum*9;
        }else { // 2~
            reqUri = "http://openapi.foodsafetykorea.go.kr/api/"+getApiKey()+"/COOKRCP01/json/"
                    +(((pageNum-1)*9)+1)+"/"+ pageNum*9;
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
                    +pageNum+"/"+ pageNum*9+"/RCP_PAT2="+category;
        }else { // 2~
            reqUri = "http://openapi.foodsafetykorea.go.kr/api/"+getApiKey()+"/COOKRCP01/json/"
                    +(((pageNum-1)*9)+1)+"/"+ pageNum*9+"/RCP_PAT2="+category;
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

    public Map<String, Object> getRecipe(String name, String category, String id) throws ParseException, JsonProcessingException {

        String reqUri = "http://openapi.foodsafetykorea.go.kr/api/"+getApiKey()+"/COOKRCP01/json/1/1"+"/RCP_NM="+name;

        RestTemplate template = new RestTemplate();

        String jsonString = template.getForObject(reqUri, String.class); // 스트링으로 받아야 그대로 받음.
        log.info("레시피 리스트 요청 응답 데이터: {}", jsonString);

        // api요청으로 받아온 json데이터의 요리 카테고리 추출
        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString); // 파싱

        JSONObject cookrcp01 = (JSONObject) jsonObject.get("COOKRCP01");
        JSONArray recipe = (JSONArray) cookrcp01.get("row"); // 배열 추출

        log.info("배열: {}", recipe);

        JSONObject recipeObj = (JSONObject) recipe.get(0); // 배열 안 객체 추출
        String rcpCategory = (String) recipeObj.get("RCP_PAT2"); // 객체 안의 키의 값 추출
        String rcpNm = (String) recipeObj.get("RCP_NM"); // 객체 안의 키의 값 추출
        log.info("보기: {}, {}",rcpCategory, rcpNm);

        // 매개변수로 받은 카테고리 != api요청에 따라 받은 요리의 카테고리
        if(!category.equals(rcpCategory) || !name.equals(rcpNm)){
            throw new IllegalArgumentException("해당 카테고리와 아이디에 맞는 레시피를 가져올 수 없습니다!");
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> returnMap = mapper.readValue(jsonString, Map.class);
        return returnMap;

    }

    // 좋아요 기능
    public LikeResponseDTO insert(final LikeRequestDTO likeRequestDTO,
                                  final String userId)
            throws Exception{
        log.info("서비스 - dto: {}", likeRequestDTO);
        log.info("서비스 - 찜 누른 유저: {}",userId);
        // 유저만 좋아요할 수 있으므로 유저 조회
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("회원 정보가 없습니다.")
        );

        // 좋아요한 레시피 조회
        String reqUri = "http://openapi.foodsafetykorea.go.kr/api/"+getApiKey()+"/COOKRCP01/json/1/1"+"/RCP_NM="+likeRequestDTO.getRecipeName();

        RestTemplate template = new RestTemplate();

        String jsonString = template.getForObject(reqUri, String.class); // 스트링으로 받아야 그대로 받음.
        log.info("레시피 리스트 요청 응답 데이터: {}", jsonString);

        // api요청으로 받아온 json데이터의 요리 이름 추출
        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString); // 파싱

        JSONObject cookrcp01 = (JSONObject) jsonObject.get("COOKRCP01");
        JSONArray recipe = (JSONArray) cookrcp01.get("row"); // 배열 추출
//        log.info("배열: {}", recipe);

        JSONObject recipeObj = (JSONObject) recipe.get(0); // 배열 안 객체 추출
        String rcpNm = (String) recipeObj.get("RCP_NM"); // 객체 안의 키의 값 추출

        log.info("좋아요 요청한 레시피: {}, 조회한 레시피: {}",likeRequestDTO.getRecipeName(), rcpNm);

//        if(!likeRequestDTO.getRecipeName().equals(rcpNm)){
//            throw new IllegalArgumentException("해당 레시피를 가져올 수 없습니다!");
//        }
        boolean dtoDone = likeRequestDTO.isDone();
        System.out.println("likeRequestDTO.isDone(): " + dtoDone);

        // 디비에서 조회
//        Like like = likeRepository.findByRecipeName(likeRequestDTO.getRecipeName()).orElseThrow(null);

        if(dtoDone){ // 요청으로 전달된 dto의 done 값이 true라면 (like는 null일 것)
            // 디비에서 조회
            boolean like = likeRepository.findByRecipeName(likeRequestDTO.getRecipeName()).isPresent(); //false=테이블에 없음
            log.info("false여야 찜 테이블에 존재하지 않는 것: {}", like);
            if (like) throw new RuntimeException("이미 찜한 레시피입니다!!!");
            
            Like saved = likeRepository.save(Like.builder().recipeName(likeRequestDTO.getRecipeName()).user(user).build());
            log.info("table saved!!!: {}", saved);
        } else {
            // 디비에서 조회
            boolean likeCheck = likeRepository.findByRecipeName(likeRequestDTO.getRecipeName()).isPresent(); // true=테이블에 존재
            log.info("true여야 테이블에 존재하는 것: {}", likeCheck);
            if (!likeCheck) throw new RuntimeException("찜목록에 존재하지 않아 삭제 실패!!!");

            Like like = likeRepository.findByRecipeName(likeRequestDTO.getRecipeName()).orElseThrow();
            log.info("전달받은 dto의 done값은 false여야 맞음: {}", likeRequestDTO.isDone());
            likeRepository.delete(Objects.requireNonNull(like));
            log.info("table deleted!!!");
        }

        return LikeResponseDTO.builder()
                .done(likeRequestDTO.isDone())
                .recipeName(likeRequestDTO.getRecipeName())
                .userId(userId)
                .build(); // 찜목록에서 추가되거나 삭제된 것을 응답.

//        // 이미 좋아요되어있으면 에러 반환
//        if (likeRepository.findByUserAndRecipe(user, recipe).isPresent()){
//            //TODO 409에러로 변경
//            throw new Exception("이미 좋아요한 레시피는 좋아요 할 수 없음!");
//        }

    }

//    public void delete(LikeRequestDTO likeRequestDTO) {
//        User user = userRepository.findById(String.valueOf(likeRequestDTO.getUserId()))
//                .orElseThrow(() -> new NotFoundException("Could not found user id : " + likeRequestDTO.getUserId()));
//
//        Recipe recipe = recipeRepository.findById(Integer.valueOf(likeRequestDTO.getRecipeName()))
//                .orElseThrow(() -> new NotFoundException("Could not found recipe id : " + likeRequestDTO.getRecipeName()));
//
//        Like like = likeRepository.findByUserAndRecipe(user, recipe)
//                .orElseThrow(() -> new NotFoundException("Could not found like id"));
//
//        likeRepository.delete(like);

//    }
}
