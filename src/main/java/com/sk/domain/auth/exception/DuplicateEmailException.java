package com.sk.domain.auth.exception;

import com.sk.global.exception.BusinessException;
import com.sk.global.exception.errorcode.AuthErrorCode;

public class DuplicateEmailException extends BusinessException {
    public DuplicateEmailException() {
        super(AuthErrorCode.DUPLICATE_EMAIL);
    }
}
