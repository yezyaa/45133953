package com.sk.global.dto;

import com.sk.global.exception.ErrorDetail;
import com.sk.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 에러 응답 구조
@Getter
public class ApiErrorResponse extends ApiResponse {
    private final ErrorDetail errorDetail;

    public ApiErrorResponse(HttpStatus status, String path, String type, ErrorCode errorCode) {
        super(false, status, path);
        this.errorDetail = ErrorDetail.of(type, errorCode);
    }

    public static ApiErrorResponse of(HttpStatus status, String path, String type, ErrorCode errorCode) {
        return new ApiErrorResponse(status, path, type, errorCode);
    }
}
