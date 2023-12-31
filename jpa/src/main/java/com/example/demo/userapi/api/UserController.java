package com.example.demo.userapi.api;

import com.example.demo.auth.TokenUserInfo;
import com.example.demo.chatapi.util.SHA256;
import com.example.demo.userapi.dto.request.LoginRequestDTO;
import com.example.demo.userapi.dto.request.UserModifyRequestDTO;
import com.example.demo.userapi.dto.request.UserSignUpRequestDTO;
import com.example.demo.userapi.dto.response.LoginResponseDTO;
import com.example.demo.userapi.dto.response.UserInfoResponseDTO;
import com.example.demo.userapi.dto.response.UserModifyResponseDTO;
import com.example.demo.userapi.dto.response.UserSignUpResponseDTO;
import com.example.demo.userapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    private final UserService userService;


    // 이메일 중복 확인 요청
    @GetMapping("/check")
    public ResponseEntity<?> check(String email){
        if(email.trim().isEmpty()){
            return ResponseEntity.badRequest()
                    .body("이메일이 없습니다");
        }
        boolean emailCheck = userService.isDuplicate(email);
        log.info("{} 중복? - {}", email, emailCheck);

        return ResponseEntity.ok().body(emailCheck);
    };


    //회원가입 요청 처리
    @PostMapping("/join")
    public ResponseEntity<?> signup(
            @Validated @RequestBody UserSignUpRequestDTO dto, BindingResult result
            ){
        log.info("/api/user/join POST! - {}", dto);


        if (result.hasErrors()){
            log.warn(result.toString());
            return ResponseEntity.badRequest()
                    .body(result.getFieldError());
        }
        try {
            UserSignUpResponseDTO responseDTO = userService.create(dto);
            return ResponseEntity.ok()
                    .body(responseDTO);
        } catch (RuntimeException e) {
            log.warn("이메일 중복!");
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn("기타 예외가 발생했습니다!");
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }//ctrl + alt + t 예외처리 try - catch 둘러싸기
    }

    // 로그인 요청 처리
    @PostMapping
    public ResponseEntity<?> signIn(
            @Validated @RequestBody LoginRequestDTO dto
    ) {
        log.info("/api/user: POST");

        try {
            LoginResponseDTO responseDTO
                    = userService.authenticate(dto);

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }


    @GetMapping("/kakaoLogin")
    public ResponseEntity<?> kakaoLogin(String code){
        log.info("/api/user/kakakoLogin - GET! - code: {}", code);
        LoginResponseDTO responseDTO = userService.kakaoService(code);

        return ResponseEntity.ok().body(responseDTO);
    }


    @GetMapping("/naverLogin")
    public ResponseEntity<?> naverLogin(@RequestParam("code") String code){
        log.info("/api/user/naverLogin - GET! - code: {}", code);
        log.info("네이버로그인 핸들러 들어옴");
        LoginResponseDTO responseDTO = userService.naverService(code);

        return ResponseEntity.ok().body(responseDTO);
    }

    // 로그아웃 처리 카카오와 일반 회원
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal TokenUserInfo userInfo){
        log.info("/api/user/logout - GET!: {}", userInfo.getEmail());

        String result = userService.logout(userInfo); // 카카오 로그인 = 아이디 || 원래 회원 = null


        return ResponseEntity.ok().body(result);

    }

    // 암호화된 userid
    @GetMapping("/userid")
    public ResponseEntity<?> getUserId(@AuthenticationPrincipal TokenUserInfo userInfo) {
        log.info("/api/user/userid - user: {}", userInfo);
        return ResponseEntity.ok().body(SHA256.encrypt(userInfo.getUserId()));
    }

    // 이메일 인증의 인증 코드를 보내는 부분
    @PostMapping("/email-auth")
    public ResponseEntity<?> emailAuth(@RequestBody HashMap<String, String> map) {
        String email = map.get("email");
        log.info("/api/user/email-auth - email: {}", email);
        try {
            String token = userService.emailAuthenticate(email);
            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            log.warn("/api/user/email-auth", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // 이메일 인증의 인증 코드를 확인하는 부분
    @PostMapping("/email-auth-check")
    public ResponseEntity<?> emailAuthCheck(@RequestBody HashMap<String, String> map) {
        String token = map.get("token");
        String authcode = map.get("authcode");
        log.info("/api/user/email-auth - {} {}", token, authcode);
        try {
            String resToken = userService.emailAuthenticateCheck(token, authcode);

            if (resToken != null) {
                return ResponseEntity.ok().body(resToken);
            } else {
                return ResponseEntity.badRequest().body("잘못된 인증코드입니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    // 유저 수정
    @PutMapping
    public ResponseEntity<?> userModify(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @RequestBody UserModifyRequestDTO requestDTO,
            BindingResult result
    ) {
        log.info("/api/user - PUT , userId:{}, dto: {}", userInfo.getUserId(), requestDTO);

        if (result.hasErrors()){
            log.warn(result.toString());
            return ResponseEntity.badRequest()
                    .body(result.getFieldError());
        }

        try {
            UserModifyResponseDTO responseDTO = userService.modify(userInfo.getUserId(), requestDTO);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.warn("/api/user - PUT", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 유저 정보
    @GetMapping
    public ResponseEntity<?> userInfo(
            @AuthenticationPrincipal TokenUserInfo userInfo
    ) {
        log.info("/api/user - GET , userId:{}", userInfo.getUserId());

        try {
            UserInfoResponseDTO responseDTO = userService.getUserInfo(userInfo.getUserId());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.warn("/api/user - GET", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
