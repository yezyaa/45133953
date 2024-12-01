package com.sk.domain.auth.api;

import com.sk.domain.auth.api.dto.SignInRequest;
import com.sk.domain.auth.api.dto.SignInResponse;
import com.sk.domain.auth.api.dto.SignUpRequest;
import com.sk.domain.auth.application.AuthService;
import com.sk.global.dto.ApiSuccessResponse;
import com.sk.global.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 로그인 인증 테스트 API
    @GetMapping("/data")
    public ResponseEntity<ApiSuccessResponse<String>> getData() {
        return ResponseEntity.ok()
                .body(ApiSuccessResponse.of(
                        HttpStatus.OK,
                        "/api/auth/data",
                        "인증 성공"
                ));
    }

    // 로그아웃
    @PostMapping("/sign-out")
    public ResponseEntity<ApiSuccessResponse<Void>> signOut(HttpServletRequest servRequest) {
        String accessToken = jwtTokenProvider.resolveToken(servRequest);
        authService.signOut(accessToken);
        return ResponseEntity.ok()
                .body(ApiSuccessResponse.of(
                        HttpStatus.OK,
                        servRequest.getServletPath(),
                        null
                ));
    }

}
