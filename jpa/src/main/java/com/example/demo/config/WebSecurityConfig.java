package com.example.demo.config;

import com.example.demo.filter.JwtAuthFilter;
import com.example.demo.filter.JwtExcetionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity // 시큐리티 설정 파일로 사용할 클래스 선언
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtExcetionFilter jwtExcetionFilter;

    //비밀번호 암호화를 위한 빈
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Security 모듈이 기본적으로 제공하는 보안 정책 해제
        http
                .cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                // 세션인증을 사용하지 않겠다
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest().permitAll();


        // 토큰 인증 필터 연결
        // jwtAuthFilter부터 연결 -> CORS 필터를 이후에 통과하도록 설정.
        http.addFilterAfter(
                jwtAuthFilter, //  CorsFilter 전에 필터 써주기!
                CorsFilter.class // import 주의: 스프링 꺼로
        );

        // Exception Filter를 Auth Filter 앞에 배치를 하겠다는 뜻.
        http.addFilterBefore(jwtExcetionFilter, JwtAuthFilter.class);

        return http.build();
    }
}