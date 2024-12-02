package com.sk.global.config;

import com.sk.global.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/h2-console/**",
                                "/",
                                "/index.html",
                                "/boardList.html",
                                "/signIn.html",
                                "/signUp.html",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/sign-up", "/api/auth/sign-in").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/sign-out").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/board").permitAll()                  // 게시글 목록 조회
                        .requestMatchers(HttpMethod.GET, "/api/board/{boardId}").permitAll()        // 게시글 상세 조회
                        .requestMatchers(HttpMethod.POST, "/api/board").authenticated()             // 게시글 작성
                        .requestMatchers(HttpMethod.PUT, "/api/board/{boardId}").authenticated()    // 게시글 수정
                        .requestMatchers(HttpMethod.DELETE, "/api/board/{boardId}").authenticated() // 게시글 삭제
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions().sameOrigin()) // H2 콘솔 iframe 허용
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 비활성화
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
