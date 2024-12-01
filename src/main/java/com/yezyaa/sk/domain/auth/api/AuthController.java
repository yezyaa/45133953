package com.yezyaa.sk.domain.auth.api;

import com.yezyaa.sk.domain.auth.api.dto.SignInRequest;
import com.yezyaa.sk.domain.auth.api.dto.SignInResponse;
import com.yezyaa.sk.domain.auth.api.dto.SignUpRequest;
import com.yezyaa.sk.domain.auth.application.AuthService;
import com.yezyaa.sk.global.dto.ApiSuccessResponse;
import com.yezyaa.sk.global.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<ApiSuccessResponse<Void>> signUp (
            @Valid @RequestBody SignUpRequest signUpRequest,
            HttpServletRequest servRequest
    ) {
        authService.signUp(signUpRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiSuccessResponse.of(
                        HttpStatus.CREATED,
                        servRequest.getServletPath(),
                        null
                ));
    }

    // 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<ApiSuccessResponse<SignInResponse>> signIn(
            @Valid @RequestBody SignInRequest signInRequest,
            HttpServletRequest servRequest
    ) {
        String accessToken = authService.signIn(signInRequest);
        SignInResponse signInResponse = new SignInResponse(accessToken);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + accessToken) // 헤더에 토큰 포함
                .body(ApiSuccessResponse.of(
                        HttpStatus.OK,
                        servRequest.getServletPath(),
                        signInResponse
                ));
    }
}
