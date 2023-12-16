package com.example.demo.auth;

import com.example.demo.userapi.entity.Role;
import com.example.demo.userapi.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Component
@Slf4j
// 역할: 토큰을 발급하고, 서명 위조를 검사하는 객체.
public class TokenProvider {

    // 서명에 사용할 값 (512비트 이상의 랜덤 문자열)
    // @Value: properties 형태의 파일의 내용을 읽어서 변수에 대입하는 아노테이션. (yml도 가능)
    @Value("${jwt.secret}")
    private String SECRET_KEY;



    // 토큰 생성 메서드
    /**
     * JSON Web Token을 생성하는 메서드
     * @param userEntity - 토큰의 내용(클레임)에 포함될 유저 정보
     * @return - 생성된 JSON을 암호화 한 토큰값
     */
    public String createToken(@NotNull User userEntity) {

        // 토큰 만료 시간 생성
        Date expiry = Date.from(
                Instant.now().plus(6, ChronoUnit.HOURS)
        );

        // 토큰 생성
    /*
        {
            "iss": "서비스 이름(발급자)",
            "exp": "2023-12-27(만료일자)",
            "iat": "2023-11-27(발급일자)",
            "email": "로그인한 사람 이메일",
            "role": "Premium"
            ...
            == 서명
        }
     */

        // 추가 클레임 정의
        Map<String, String> claims = new HashMap<>();
        claims.put("email", userEntity.getEmail());
        claims.put("role", String.valueOf(userEntity.getRole()));


        return Jwts.builder()
                // token header에 들어갈 서명
                .signWith(
                        Keys.hmacShaKeyFor(SECRET_KEY.getBytes()),
                        SignatureAlgorithm.HS512
                )
                // token payload에 들어갈 클레임 설정.
                .setClaims(claims) // 추가 클레임은 먼저 설정해야 함.
                .setIssuer("1nterface운영자") // iss: 발급자 정보
                .setIssuedAt(new Date()) // iat: 발급 시간
                .setExpiration(expiry) // exp: 만료 시간
                .setSubject(userEntity.getId()) // sub: 토큰을 식별할 수 있는 주요 데이터
                .compact(); // String 리턴.


    }


    // 토큰 위조 여부 확인
    public TokenUserInfo validateAndGetTokenUserInfo(String token) {
        // 사용자가 로그인 후 어떠한 요청을 할때 토큰값이 브라우저에서 같이 넘어오면
        // 그 토큰 값이 위조가 되었는지를 검사하기위해서 토큰 -> 객체 형태로
        // 풀어주는곳
        Claims claims = Jwts.parserBuilder()
                    // 토큰 발급자의 발급 당시의 서명을 넣어줌
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    // 서명 위조 검사: 위조된 경우에는 예외가 발생합니다.
                    // 위조가 되지 않은 경우 payload를 리턴
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            log.info("claims: {}", claims);

        return TokenUserInfo.builder()
                .userId(claims.getSubject())
                .email(claims.get("email", String.class))
                .role(Role.valueOf(claims.get("role", String.class)))
                .build(); // claims 중에 쓰고 싶은 것만 뽑아 TokenUserInfo 객체로 포장

    }





}
