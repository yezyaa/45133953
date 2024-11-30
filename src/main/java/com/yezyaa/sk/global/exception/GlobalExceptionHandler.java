package com.yezyaa.sk.global.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yezyaa.sk.global.dto.ApiErrorResponse;
import com.yezyaa.sk.global.exception.errorcode.ErrorCode;
import com.yezyaa.sk.global.exception.errorcode.JsonErrorCode;
import com.yezyaa.sk.global.exception.errorcode.ValidationErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 전역 예외 처리 핸들러
@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class GlobalExceptionHandler {
    private static final String LOG_FORMAT = "Class: {}, Status: {}, Message: {}";

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> businessException(
            HttpServletRequest request,
            BusinessException e
    ) {
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = errorCode.getStatus();
        String simpleName = e.getClass().getSimpleName();
        String message = errorCode.getMessage();

        log.error(LOG_FORMAT, simpleName, status, message);

        return ResponseEntity
                .status(status)
                .body(ApiErrorResponse.of(
                        status,
                        request.getServletPath(),
                        simpleName,
                        errorCode
                ));
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ApiErrorResponse> handleJsonProcessingException(
            HttpServletRequest request,
            JsonProcessingException e
    ) {
        String simpleName = e.getClass().getSimpleName();
        String message = e.getOriginalMessage();

        log.error(LOG_FORMAT, simpleName, HttpStatus.BAD_REQUEST, message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.of(
                        HttpStatus.BAD_REQUEST,
                        request.getServletPath(),
                        simpleName,
                        new JsonErrorCode(message)
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
            HttpServletRequest request,
            MethodArgumentNotValidException e
    ) {
        String simpleName = e.getClass().getSimpleName();
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        log.error(LOG_FORMAT, simpleName, HttpStatus.BAD_REQUEST, message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.of(
                        HttpStatus.BAD_REQUEST,
                        request.getServletPath(),
                        simpleName,
                        new ValidationErrorCode(message)
                ));
    }
}
