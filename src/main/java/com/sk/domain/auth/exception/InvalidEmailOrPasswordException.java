package com.sk.domain.auth.exception;

import com.sk.global.exception.BusinessException;
import com.sk.global.exception.errorcode.AuthErrorCode;

public class InvalidEmailOrPasswordException extends BusinessException {
    public InvalidEmailOrPasswordException() {
        super(AuthErrorCode.INVALID_EMAIL_OR_PASSWORD);
    }
}
