package com.yezyaa.sk.global.exception;

import com.yezyaa.sk.global.exception.errorcode.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 에러 상세 정보 구조
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorDetail {
    private final String type;
    private final String message;

    public static ErrorDetail of(String type, ErrorCode errorCode){
        return new ErrorDetail(type, errorCode.getMessage());
    }

    public static ErrorDetail from(ErrorCode errorCode) {
        return new ErrorDetail(
                "UnknownException",
                errorCode.getMessage()
        );
    }
}
