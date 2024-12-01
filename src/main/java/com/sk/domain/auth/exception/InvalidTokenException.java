package com.sk.domain.auth.exception;

import com.sk.global.exception.BusinessException;
import com.sk.global.exception.errorcode.AuthErrorCode;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException() {
        super(AuthErrorCode.INVALID_TOKEN);
    }
}
