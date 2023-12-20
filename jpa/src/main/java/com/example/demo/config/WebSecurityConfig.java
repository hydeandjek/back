package com.example.demo.config;

import com.example.demo.filter.JwtAuthFilter;
import com.example.demo.filter.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;


@EnableWebSecurity // 시큐리티 설정 파일로 사용할 클래스 선언.
@RequiredArgsConstructor // final로 선언한 것이 이 아노테이션 덕분에 주입이 된다.
// 자동 권한 검사를 수행하기 위한 설정
@EnableGlobalMethodSecurity(prePostEnabled = true) // 메서드 호출 전에 권한을 허용하는 메서드
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Security 모듈이 기본적으로 제공하는 보안 정책 해제.
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                // 세션인증을 사용하지 않겠다.
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .anyRequest().permitAll();

//        http
//                .cors()
//                .and()
//                .csrf().disable()
//                .httpBasic().disable()
//                // 세션인증을 사용하지 않겠다
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .anyRequest().permitAll();

        // 토큰 인증 필터 연결
        // jwtAuthFilter 부터 연결후 CorsFilter를 이후에 통과하도록 해야 됨
        http.addFilterAfter(
                jwtAuthFilter,
                CorsFilter.class // import 주의: 스프링 꺼로
        );

        // Exception Filter를 Auth Filter 앞에 배치를 하겠다는 뜻.
        // Filter 역할을 하는 클래스는 Spring Container 내부에 배치되는 것이 아니기 때문에
        // Spring이 제공하는 예외 처리 등이 힘들 수 있다.
        // 예외 처리만을 전담하는 필터를 생성해서, 예외가 발생하는 필터 앞단에 배치하면 예외가 먼저 배치된 필터로
        // 넘어가서 처리가 가능하게 됩니다.
        http.addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class);

        return http.build();
    }

}
