package com.yezyaa.sk.global.exception.errorcode;

import org.springframework.http.HttpStatus;

// 공통 ErrorCode 인터페이스
public interface ErrorCode {
    HttpStatus getStatus();
    String getMessage();
}
