package com.yezyaa.sk.global.exception;

import com.yezyaa.sk.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 공통 비즈니스 예외 클래스
@Getter
@RequiredArgsConstructor
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
}
