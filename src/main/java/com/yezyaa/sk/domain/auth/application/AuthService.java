package com.yezyaa.sk.domain.auth.application;

import com.yezyaa.sk.domain.auth.api.dto.SignUpRequest;
import com.yezyaa.sk.domain.auth.domain.Member;
import com.yezyaa.sk.domain.auth.exception.DuplicateEmailException;
import com.yezyaa.sk.domain.auth.exception.PasswordMismatchException;
import com.yezyaa.sk.domain.auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public void signUp(SignUpRequest signUpRequest) {

        // 이메일 중복 확인
        if (authRepository.existsByEmail(signUpRequest.email())) {
            throw new DuplicateEmailException();
        }

        // 비밀번호 확인
        if (!signUpRequest.password().equals(signUpRequest.passwordCheck())) {
            throw new PasswordMismatchException();
        }

        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(signUpRequest.password());

        Member newMember = signUpRequest.toEntity(encodedPassword);
        authRepository.save(newMember);
    }
}
