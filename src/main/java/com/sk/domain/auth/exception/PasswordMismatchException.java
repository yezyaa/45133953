package com.sk.domain.auth.exception;

import com.sk.global.exception.BusinessException;

public class PasswordMismatchException extends BusinessException {
    public PasswordMismatchException() {
        super(AuthErrorCode.PASSWORD_MISMATCH);
    }
}
