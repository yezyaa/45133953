package com.yezyaa.sk.domain.auth.exception;

import com.yezyaa.sk.global.exception.BusinessException;
import com.yezyaa.sk.global.exception.errorcode.AuthErrorCode;

public class DuplicateEmailException extends BusinessException {
    public DuplicateEmailException() {
        super(AuthErrorCode.DUPLICATE_EMAIL);
    }
}
