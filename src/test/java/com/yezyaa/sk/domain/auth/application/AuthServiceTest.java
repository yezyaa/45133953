package com.yezyaa.sk.domain.auth.application;

import com.yezyaa.sk.domain.auth.api.dto.SignUpRequest;
import com.yezyaa.sk.domain.auth.domain.Member;
import com.yezyaa.sk.domain.auth.exception.DuplicateEmailException;
import com.yezyaa.sk.domain.auth.exception.PasswordMismatchException;
import com.yezyaa.sk.domain.auth.repository.AuthRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void clean() {
        authRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUpSuccess() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
                "yezy@gmail.com",
                "이예지",
                "password123",
                "password123"
        );

        // when
        authService.signUp(signUpRequest);

        // then
        assertEquals(1, authRepository.count());

        Member member = authRepository.findAll().iterator().next();
        assertEquals("yezy@gmail.com", member.getEmail());
        assertEquals("이예지", member.getName());
        Assertions.assertTrue(passwordEncoder.matches("password123", member.getPassword()));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    void signUpFailsWithDuplicateEmail() {
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
        assertThrows(DuplicateEmailException.class, () -> authService.signUp(signUpRequest));
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 불일치")
    void signUpFailsWithPasswordMismatch() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
                "yezy@gmail.com",
                "이예지",
                "password123",
                "password1234"
        );

        // expected
        assertThrows(PasswordMismatchException.class, () -> authService.signUp(signUpRequest));
    }
}