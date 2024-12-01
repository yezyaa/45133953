package com.yezyaa.sk.domain.auth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yezyaa.sk.domain.auth.api.dto.SignInRequest;
import com.yezyaa.sk.domain.auth.api.dto.SignUpRequest;
import com.yezyaa.sk.domain.auth.domain.Member;
import com.yezyaa.sk.domain.auth.repository.AuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void clean() {
        authRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
                "yezy@gmail.com",
                "이예지",
                "password123",
                "password123"
        );

        // expected
        mockMvc.perform(post("/api/auth/sign-up")
                        .content(objectMapper.writeValueAsString(signUpRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    void signUpFailsWithDuplicateEmail() throws Exception {
        // given
        authRepository.save(Member.of(
                "yezy@gmail.com",
                "이예지",
                passwordEncoder.encode("password123")
        ));

        SignUpRequest signUpRequest = new SignUpRequest(
                "yezy@gmail.com",
                "이예지",
                "password123",
                "password123"
        );

        // expected
        mockMvc.perform(post("/api/auth/sign-up")
                        .content(objectMapper.writeValueAsString(signUpRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 유효성 검증(잘못된 이메일 형식)")
    void signUpFailsWithInvalidEmailFormat() throws Exception {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
                "yezy#gmail.com",
                "이예지",
                "password123",
                "password123"
        );

        // expected
        mockMvc.perform(post("/api/auth/sign-up")
                        .content(objectMapper.writeValueAsString(signUpRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 유효성 검증(필드 누락)")
    void signUpFailsWithMissingFields() throws Exception {
        // given
        String invalidRequest = """
                {
                  "name": "이예지",
                  "password": "password123",
                  "passwordCheck": "password123"
                }
                """;

        // expected
        mockMvc.perform(post("/api/auth/sign-up")
                        .content(invalidRequest)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 성공 - AccessToken 발급")
    void signInSuccess() throws Exception {
        // given
        authRepository.save(Member.of(
                "yezy@gmail.com",
                "이예지",
                passwordEncoder.encode("password123")
        ));

        SignInRequest signInRequest = new SignInRequest(
                "yezy@gmail.com",
                "password123"
        );

        // when
        mockMvc.perform(post("/api/auth/sign-in")
                        .content(objectMapper.writeValueAsString(signInRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk()) // 200 OK 응답 예상
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty()) // 응답 JSON에서 accessToken 확인
                .andExpect(header().string("Authorization", org.hamcrest.Matchers.startsWith("Bearer "))) // Authorization 헤더 검증
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    void signInFailsWithInvalidEmail() throws Exception {
        // given
        SignInRequest signInRequest = new SignInRequest(
                "yezy@gmail.com",
                "password123"
        );

        // expected
        mockMvc.perform(post("/api/auth/sign-in")
                        .content(objectMapper.writeValueAsString(signInRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void signInFailsWithInvalidPassword() throws Exception {
        // given
        authRepository.save(Member.of(
                "yezy@gmail.com",
                "이예지",
                passwordEncoder.encode("password123")
        ));

        SignInRequest signInRequest = new SignInRequest(
                "yezy@gmail.com",
                "password1234"
        );

        // expected
        mockMvc.perform(post("/api/auth/sign-in")
                        .content(objectMapper.writeValueAsString(signInRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 이메일 형식")
    void signInFailsWithInvalidEmailFormat() throws Exception {
        // given
        SignInRequest signInRequest = new SignInRequest(
                "yezy#gmail.com",
                "password123"
        );

        // expected
        mockMvc.perform(post("/api/auth/sign-in")
                        .content(objectMapper.writeValueAsString(signInRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}