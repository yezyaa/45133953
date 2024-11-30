package com.yezyaa.sk.global.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/*
 * 클라이언트에게 응답을 보낼 때 사용되는 공통적인 응답 구조 정의
 * - 응답 성공 시, data
 * - 예외 발생 시, error
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse {
    private final Boolean success;      // 요청 성공 여부 (true/false)
    private final HttpStatus status;    // HTTP 상태 코드
    private final String path;          // 요청 경로
}
