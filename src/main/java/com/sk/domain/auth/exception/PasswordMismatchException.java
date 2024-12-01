package com.sk.domain.auth.exception;

import com.sk.global.exception.BusinessException;
import com.sk.global.exception.errorcode.AuthErrorCode;

public class PasswordMismatchException extends BusinessException {
    public PasswordMismatchException() {
        super(AuthErrorCode.PASSWORD_MISMATCH);
    }
}
