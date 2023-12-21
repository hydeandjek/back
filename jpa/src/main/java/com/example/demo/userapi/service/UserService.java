package com.example.demo.userapi.service;

import com.example.demo.auth.TokenProvider;
import com.example.demo.auth.TokenUserInfo;
import com.example.demo.userapi.dto.request.LoginRequestDTO;
import com.example.demo.userapi.dto.request.UserRequestSignUpDTO;
import com.example.demo.userapi.dto.response.KakaoUserDTO;
import com.example.demo.userapi.dto.response.LoginResponseDTO;
import com.example.demo.userapi.dto.response.NaverUserDTO;
import com.example.demo.userapi.dto.response.UserSignUpResponseDTO;
import com.example.demo.userapi.entity.LoginType;
import com.example.demo.userapi.entity.SnsLogin;
import com.example.demo.userapi.entity.User;
import com.example.demo.userapi.repository.SnsLoginRepository;
import com.example.demo.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SnsLoginRepository snsLoginRepository;

    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Value("${kakao.client_id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.redirect_url}")
    private String KAKAO_REDIRECT_URI;

    @Value("${kakao.client_secret}")
    private String KAKAO_CLIENT_SECRET;


    @Value("${naver.client_id}")
    private String NAVER_CLIENT_ID;


    @Value("${naver.client_secret}")
    private String NAVER_CLIENT_SECRET;




    public UserSignUpResponseDTO create(final UserRequestSignUpDTO dto, final String uploadedFilePath) {
        String email = dto.getEmail();

        if (isDuplicate(email)){
            log.warn("이메일이 중복되었습니다. - {}", email);
            throw new RuntimeException("중복된 이메일 입니다.");
        }

        // 패스워드 인코딩
        String encoded = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encoded);

        // dto를 User Entity로 변환해서 저장
        User saved = userRepository.save(dto.toEntity(uploadedFilePath));
        log.info("회원 가입 정상 수행됨! - saved user - {}", saved);

        return new UserSignUpResponseDTO(saved);
    }

    public boolean isDuplicate(String email) {
        return userRepository.existsByEmail(email);
        // 이메일 중복 확인
    }

    public LoginResponseDTO authenticate(LoginRequestDTO dto) {
        //  이메일을 통해 회원 정보 조회
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(
                        () -> new RuntimeException("가입된 회원이 아닙니다.")
                );

        // 패스워드 검증
        String rawPassword = dto.getPassword(); // 입력한 비번
        String encodedPassword = user.getPassword(); // DB에 저장된 암호화된 비번

        if(!passwordEncoder.matches(rawPassword, encodedPassword)){
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        log.info("{}님 로그인 성공!", user.getUserName());

        // 로그인 성공 후에 클라이언트에게 뭘 리턴할 것인가???
        // -> JWT를 클라이언트에게 발급해 주어야 한다.
        //여기까지 오면 로그인에 문제 없음
        String token = tokenProvider.createToken(user);

        return new LoginResponseDTO(user, token);
    }

    public LoginResponseDTO kakaoService(final String code) {

        // 인가코드를 통해 토큰 발급받기  착각해서 이름 바꿈 원래 string accessToken임
        Map<String, Object> responseData = getKakaoAccessToken(code);
        log.info("token: {}", responseData.get("access_token"));

        // 토큰을 통해 사용자 정보 가져오기
        KakaoUserDTO dto = getKakaoUserInfo((String) responseData.get("access_token"));
        log.info("kakao dto: {}", dto );

        // 일회성 로그인으로 처리 -> dto를 바로 화면단으로 리턴
        // 회원가입 처리 -> 이메일 중복 검사 진행 -> 자체 jwt를 생성해서 토큰을 화면단에 리턴.
        // -> 화면단에서는 적절한 url을 선택하여 redirect를 진행.

        if(!isDuplicate(dto.getKakaoAccount().getEmail())){
            // 이메일이 중복되지 않았다 -> 이전에 로그인 한 적이 없음 -> DB에 데이터 세팅
            // 새로운 user와 snslogin 생성
            SnsLogin snsLogin = SnsLogin.builder()
                    .loginType(LoginType.KAKAO)
                    .accessToken((String) responseData.get("access_token"))
                    .build();

            User saved = userRepository.save(dto.toEntity(snsLogin));
            log.info("kakao Service {}",saved );
            userRepository.save(saved);

        }
        // 이메일이 중복됐다? -> 이전에 로그인 한 적이 있다. -> DB에 데이터를 또 넣을 필요는 없다.
        // 조회한다
        User foundUser = userRepository.findByEmail(dto.getKakaoAccount().getEmail())
                .orElseThrow();

        log.info("user: {}", foundUser);



        String token = tokenProvider.createToken(foundUser);

        // 유저에 access_token 넣기
        SnsLogin snsLogin = foundUser.getSnsLogin();
        snsLogin.setAccessToken((String) responseData.get("access_token"));
        snsLogin.setLoginType(LoginType.KAKAO);
        userRepository.save(foundUser);

        return new LoginResponseDTO(foundUser, token);


    }

    private KakaoUserDTO getKakaoUserInfo(String accessToken) {

        // 요청 uri
        String requestUri = "https://kapi.kakao.com/v2/user/me";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 보내기 Spring에서 지원하는 객체로 간편하게 Rest 방식 API를 호출할 수 있는 Spring 내장 클래스
        RestTemplate template = new RestTemplate();
        ResponseEntity<KakaoUserDTO> responseEntity
                = template.exchange(requestUri, HttpMethod.GET, new HttpEntity<>(headers), KakaoUserDTO.class);

        // 응답 바디 읽기
        KakaoUserDTO responseData = responseEntity.getBody();
        log.info("user profile: {}", responseData);

        return responseData;
    }

    private Map<String, Object> getKakaoAccessToken(String code) {

        log.info("-----------------------------------------------------");

        // 요청 uri
        String requestUri = "https://kauth.kakao.com/oauth/token";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 바디(파라미터) 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); // 카카오 공식 문서 기준 값으로 세팅
        params.add("client_id", KAKAO_CLIENT_ID); // 카카오 디벨로퍼 REST API 키
        params.add("redirect_uri", KAKAO_REDIRECT_URI); // 카카오 디벨로퍼 등록된 redirect uri
        params.add("code", code); // 프론트에서 인가 코드 요청시 전달받은 코드값
        params.add("client_secret", KAKAO_CLIENT_SECRET); // 카카오 디벨로퍼 client secret(활성화 시 추가해 줘야 함)

        // 헤더와 바디 정보를 합치기 위해 HttpEntity 객체 생성
        HttpEntity<Object> requestEntity = new HttpEntity<>(params, headers);

        // 카카오 서버로 POST 통신   Spring에서 지원하는 객체로 간편하게 Rest 방식 API를 호출할 수 있는 Spring 내장 클래스
        RestTemplate template = new RestTemplate();

        // 통신을 보내면서 응답데이터를 리턴
        // param1: 요청 url
        // param2: 요청 메서드 (전송 방식)
        // param3: 헤더와 요청 파라미터정보 엔터티
        // param4: 응답 데이터를 받을 객체의 타입 (ex: dto, map)
        // 만약 구조가 복잡한 경우에는 응답 데이터 타입을 String으로 받아서 JSON-simple 라이브러리로 직접 해체.
        ResponseEntity<Map> responseEntity
                = template.exchange(requestUri, HttpMethod.POST, requestEntity, Map.class);

        // 응답 데이터에서 필요한 정보를 가져오기
        Map<String, Object> responseData = (Map<String, Object>)responseEntity.getBody();
        log.info("토큰 요청 응답 데이터: sssss{}", responseData);

        // 여러가지 데이터 중 access_token이라는 이름의 데이터를 리턴 (Object를 String으로 형 변환해서 리턴)
        return responseData;
    }



    public String logout(TokenUserInfo userInfo) {
        log.info("서비스가 작동한다.로그아웃할 유저는 {}", userInfo);
        User foundUser = userRepository.findById(userInfo.getUserId())
                .orElseThrow(); // 사용자 정보 들어있음


        SnsLogin snsLogin = foundUser.getSnsLogin();
        if (snsLogin != null) {
            String accesstoken = snsLogin.getAccessToken();
            if (accesstoken != null) {
                if (snsLogin.getLoginType().equals(LoginType.KAKAO)) {
                    String reqUri = "https://kapi.kakao.com/v1/user/logout";
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Authorization", "Bearer " + accesstoken);


                    Map<String, Object> naverOut = getNaverOut(accesstoken);


                    RestTemplate template = new RestTemplate();
                    ResponseEntity<String> responseData =
                            template.exchange(reqUri, HttpMethod.POST, new HttpEntity<>(headers), String.class);
                    return responseData.getBody();
                }

            }
        }
        return null;
    }

    private Map<String, Object> getNaverOut(String accesstoken) {

        String NaverreqUri = "https://nid.naver.com/oauth2.0/token";
        HttpHeaders Naverheaders = new HttpHeaders();
        Naverheaders.add("Authorization", "Bearer " + accesstoken);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", "naver_id"); // 애플리케이션 등록 시 발급받은 Client ID 값
        params.add("client_secret", "naver_secret_key"); // 애플리케이션 등록 시 발급받은 Client secret 값
        params.add("access_token", accesstoken); // 유효한 접근토큰 값
        params.add("grant_type", "delete"); //요청 타입. delete 으로 설정

        // 헤더와 바디 정보를 합치기 위해 HttpEntity 객체 생성
        HttpEntity<Object> requestEntity = new HttpEntity<>(params);

        RestTemplate template = new RestTemplate();

        ResponseEntity<Map> responseEntity
                = template.exchange(NaverreqUri, HttpMethod.POST, requestEntity, Map.class);

        // 응답 데이터에서 필요한 정보를 가져오기
        Map<String, Object> responseData = (Map<String, Object>)responseEntity.getBody();
        log.info("토큰 요청 응답 데이터: {}", responseData);


        return responseData;


    }

    public String generateState()
    {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    public LoginResponseDTO naverService(String code) {

        // 상태 토큰으로 사용할 랜덤 문자열 생성
        String state = generateState();
//        // 세션 또는 별도의 저장 공간에 상태 토큰을 저장
//        request.session().attribute("state", state);
//        return state;

        // 인가코드를 통해 토큰 발급받기
        Map<String, Object> responseData = getNaverAccessToken(code, state);
        log.info("token: {}", responseData.get("access_token"));

        // 토큰을 통해 사용자 정보 가져오기
        NaverUserDTO dto = getNaverUserInfo((String) responseData.get("access_token"));

        // 일회성 로그인으로 처리 -> dto를 바로 화면단으로 리턴
        // 회원가입 처리 -> 이메일 중복 검사 진행 -> 자체 jwt를 생성해서 토큰을 화면단에 리턴.
        // -> 화면단에서는 적절한 url을 선택하여 redirect를 진행.

        log.info(dto.getResponse().getEmail());



        if(!isDuplicate(dto.getResponse().getEmail())){
            // 이메일이 중복되지 않았다 -> 이전에 로그인 한 적이 없음 -> DB에 데이터 세팅
            // 새로운 snslogin 생성 필요
            SnsLogin snsLogin = SnsLogin.builder()
                    .loginType(LoginType.NAVER)
                    .accessToken((String) responseData.get("access_token"))
                    .build();

            User saved = userRepository.save(dto.toEntity(snsLogin));
            log.info("naver dto: {}", dto );
        }
        // 이메일이 중복됐다? -> 이전에 로그인 한 적이 있다. -> DB에 데이터를 또 넣을 필요는 없다.
        // 조회한다
        User foundUser = userRepository.findByEmail(dto.getResponse().getEmail())
                .orElseThrow();

        // 유저의 sns_id로 SnsLogin 테이블에서 찾아서 넣어야 함
        String token = tokenProvider.createToken(foundUser);

        SnsLogin snsLogin = foundUser.getSnsLogin();
        snsLogin.setAccessToken((String) responseData.get("access_token"));
        snsLogin.setLoginType(LoginType.NAVER);

        userRepository.save(foundUser);

        return new LoginResponseDTO(foundUser, token);

    }

    private NaverUserDTO getNaverUserInfo(String accessToken) {

        // 요청 uri
        String requestUri = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 보내기 Spring에서 지원하는 객체로 간편하게 Rest 방식 API를 호출할 수 있는 Spring 내장 클래스
        RestTemplate template = new RestTemplate();
        ResponseEntity<NaverUserDTO> responseEntity =
                template.exchange(requestUri, HttpMethod.GET, new HttpEntity<>(headers), NaverUserDTO.class);

        // 응답 바디 읽기
        NaverUserDTO responseData = responseEntity.getBody();
        log.info("user profile: {}", responseData);

        return responseData;

    }

    private Map<String, Object> getNaverAccessToken(String code, String state) {
        String requestUri = "https://nid.naver.com/oauth2.0/token";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("grant_type", "authorization_code");

        // 요청 바디(파라미터) 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); // 발급
        params.add("client_id", NAVER_CLIENT_ID); // 애플리케이션 등록 시 발급받은 Client ID 값
        params.add("client_secret", NAVER_CLIENT_SECRET); // 애플리케이션 등록 시 발급받은 Client secret 값
        params.add("code", code); // 로그인 인증 요청 API 호출에 성공하고 리턴받은 인증코드값 (authorization code)
        params.add("state", state); // 사이트 간 요청 위조(cross-site request forgery) 공격을 방지하기 위해
        // 애플리케이션에서 생성한 상태 토큰값으로 URL 인코딩을 적용한 값을 사용

        // 헤더와 바디 정보를 합치기 위해 HttpEntity 객체 생성
        HttpEntity<Object> requestEntity = new HttpEntity<>(params, headers);

        RestTemplate template = new RestTemplate();

        // 통신을 보내면서 응답데이터를 리턴
        // param1: 요청 url
        // param2: 요청 메서드 (전송 방식)
        // param3: 헤더와 요청 파라미터정보 엔터티
        // param4: 응답 데이터를 받을 객체의 타입 (ex: dto, map)
        // 만약 구조가 복잡한 경우에는 응답 데이터 타입을 String으로 받아서 JSON-simple 라이브러리로 직접 해체.
        ResponseEntity<Map> responseEntity
                = template.exchange(requestUri, HttpMethod.POST, requestEntity, Map.class);

        // 응답 데이터에서 필요한 정보를 가져오기
        Map<String, Object> responseData = (Map<String, Object>)responseEntity.getBody();
        log.info("토큰 요청 응답 데이터: {}", responseData);

        // 여러가지 데이터 중 access_token이라는 이름의 데이터를 리턴 (Object를 String으로 형 변환해서 리턴)
        return responseData;
    }


}
