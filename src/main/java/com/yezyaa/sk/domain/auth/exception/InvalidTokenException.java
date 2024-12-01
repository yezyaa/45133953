package com.yezyaa.sk.domain.auth.exception;

import com.yezyaa.sk.global.exception.BusinessException;
import com.yezyaa.sk.global.exception.errorcode.AuthErrorCode;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException() {
        super(AuthErrorCode.INVALID_TOKEN);
    }
}
