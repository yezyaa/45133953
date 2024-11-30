package com.yezyaa.sk.domain.auth.exception;

import com.yezyaa.sk.global.exception.BusinessException;
import com.yezyaa.sk.global.exception.errorcode.AuthErrorCode;

public class PasswordMismatchException extends BusinessException {
    public PasswordMismatchException() {
        super(AuthErrorCode.PASSWORD_MISMATCH);
    }
}
