package com.sk.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// 유효성 검사 관련 에러 코드
@Getter
@RequiredArgsConstructor
public class ValidationErrorCode implements ErrorCode {
    private final HttpStatus status = HttpStatus.BAD_REQUEST;
    private final String message;
}
