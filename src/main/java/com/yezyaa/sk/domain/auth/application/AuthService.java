package com.yezyaa.sk.domain.auth.application;

import com.yezyaa.sk.domain.auth.api.dto.SignInRequest;
import com.yezyaa.sk.domain.auth.api.dto.SignUpRequest;
import com.yezyaa.sk.domain.auth.domain.Member;
import com.yezyaa.sk.domain.auth.exception.DuplicateEmailException;
import com.yezyaa.sk.domain.auth.exception.InvalidEmailOrPasswordException;
import com.yezyaa.sk.domain.auth.exception.PasswordMismatchException;
import com.yezyaa.sk.domain.auth.repository.AuthRepository;
import com.yezyaa.sk.global.security.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;

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

    // 로그인
    @Transactional
    public String signIn(SignInRequest signInRequest) {
        
        // 이메일로 사용자 조회
        Member member = authRepository.findByEmail(signInRequest.email())
                .orElseThrow(InvalidEmailOrPasswordException::new);

        // 비밀번호 검증
        if (!passwordEncoder.matches(signInRequest.password(), member.getPassword())) {
            throw new InvalidEmailOrPasswordException();
        }

        // AccessToken 생성
        String accessToken = jwtTokenProvider.createAccessToken(member.getId());

        // AccessToken 저장: 로그아웃을 처리하기 위해 DB에서 AccessToken을 관리
        member.updateAccessToken(accessToken);
        return accessToken;
    }
}
