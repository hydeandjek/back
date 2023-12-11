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
                .and()
                // 어떤 요청에서 인증을 안 할 것인지, 언제 인증을 할 것인지 설정
                .authorizeRequests()
                // /api/auth/** 은 permit이지만, /promote는 검증이 필요하기 때문에 추가.( 순서 조심! )
                .antMatchers(HttpMethod.PUT, "/api/auth/promote")
                .authenticated() // 윗줄과 이 줄,  두줄을 써야 토큰이 없이 응답을 보냈을 때 요청이 필터된다
                .antMatchers("/api/auth/load-profile").authenticated()
                // '/api/auth'로 시작하는 요청과 '/'요청은 권한 검사 없이 허용하겠다.
                .antMatchers("/", "/api/auth/**").permitAll()
                // '/api/todos'라는 요청이 POST로 들어오고, Role 값이 ADMIN인 경우 검사 없이 허용하겟다.
//                .antMatchers(HttpMethod.POST, "/api/todos").hasRole("ADMIN").permitAll()
                // 위에서 따로 설정하지 않은 나머니 요청들은 권한 검사가 필요하다.
                .anyRequest().authenticated();

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