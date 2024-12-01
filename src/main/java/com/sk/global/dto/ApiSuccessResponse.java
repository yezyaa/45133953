package com.sk.global.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// 성공 응답 구조
@Getter
public class ApiSuccessResponse<T> extends ApiResponse {
    private final T data; // 응답 데이터 저장

    private ApiSuccessResponse(HttpStatus status, String path, T data) {
        super(true, status, path);
        this.data = data;
    }

    public static <T> ApiSuccessResponse<T> of(HttpStatus status, String path, T data) {
        return new ApiSuccessResponse<>(status, path, data);
    }
}
