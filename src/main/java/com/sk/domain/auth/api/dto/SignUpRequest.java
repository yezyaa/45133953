package com.sk.domain.auth.api.dto;

import com.sk.domain.auth.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpRequest (
        @NotBlank(message = "이메일을 입력해 주세요.")
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "유효한 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "이름을 입력해 주세요.")
        String name,

        @NotBlank(message = "비밀번호를 입력해 주세요.")
        String password,

        @NotBlank(message = "비밀번호 확인을 입력해 주세요.")
        String passwordCheck
) {
    public Member toEntity(String encodedPassword) {
        return Member.of(email, name, encodedPassword);
    }
}
