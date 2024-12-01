package com.sk.domain.auth.exception;

import com.sk.global.exception.BusinessException;
import com.sk.global.exception.errorcode.AuthErrorCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super(AuthErrorCode.USER_NOT_FOUND);
    }
}
