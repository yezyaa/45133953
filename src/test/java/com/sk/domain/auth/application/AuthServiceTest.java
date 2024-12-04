package com.sk.domain.auth.application;

import com.sk.domain.auth.api.dto.request.SignInRequest;
import com.sk.domain.auth.api.dto.request.SignUpRequest;
import com.sk.domain.auth.domain.Member;
import com.sk.domain.auth.exception.DuplicateEmailException;
import com.sk.domain.auth.exception.InvalidEmailOrPasswordException;
import com.sk.domain.auth.exception.InvalidTokenException;
import com.sk.domain.auth.exception.PasswordMismatchException;
import com.sk.domain.auth.repository.MemberRepository;
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
    private MemberRepository authRepository;

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

    @Test
    @DisplayName("로그인 성공 - AccessToken 발급")
    void signInSuccess() {
        // given
        Member member = authRepository.save(Member.of(
                "yezy@gmail.com",
                "이예지",
                passwordEncoder.encode("password123")
        ));

        SignInRequest signInRequest = new SignInRequest(
                "yezy@gmail.com",
                "password123"
        );

        // when
        String accessToken = authService.signIn(signInRequest);

        // then
        Member updatedMember = authRepository.findById(member.getId()).orElseThrow();
        assertEquals(updatedMember.getAccessToken(), accessToken);
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 이메일")
    void signInFailsWithInvalidEmail() {
        // given
        SignInRequest signInRequest = new SignInRequest(
                "yezy@gmail.com",
                "password123"
        );

        // expected
        assertThrows(InvalidEmailOrPasswordException.class,
                () -> authService.signIn(signInRequest));
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void signInFailsWithInvalidPassword() {
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
        assertThrows(InvalidEmailOrPasswordException.class,
                () -> authService.signIn(signInRequest));
    }

    @Test
    @DisplayName("로그아웃 성공")
    void signOutSuccess() {
        // given
        Member member = authRepository.save(Member.of(
                "yezy@gmail.com",
                "이예지",
                passwordEncoder.encode("password123")
        ));
        String accessToken = authService.signIn(new SignInRequest(
                "yezy@gmail.com",
                "password123"
        ));

        // when
        authService.signOut(accessToken);

        // then
        Member updatedMember = authRepository.findById(member.getId()).orElseThrow();
        assertNull(updatedMember.getAccessToken());
    }

    @Test
    @DisplayName("로그아웃 실패 - 유효하지 않는 토큰")
    void signOutFailsWithNonExistentUser() {
        // expected
        assertThrows(InvalidTokenException.class, ()
                -> authService.signOut("invalid_token"));
    }
}