package com.yezyaa.sk.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// JSON 관련 에러 코드
@Getter
@RequiredArgsConstructor
public class JsonErrorCode implements ErrorCode {
    private final HttpStatus status = HttpStatus.BAD_REQUEST;
    private final String message;
}
