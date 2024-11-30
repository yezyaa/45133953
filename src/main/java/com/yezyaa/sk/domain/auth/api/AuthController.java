package com.yezyaa.sk.domain.auth.api;

import com.yezyaa.sk.domain.auth.api.dto.SignUpRequest;
import com.yezyaa.sk.domain.auth.application.AuthService;
import com.yezyaa.sk.global.dto.ApiSuccessResponse;
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
}
