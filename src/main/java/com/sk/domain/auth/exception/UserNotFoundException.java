package com.sk.domain.auth.exception;

import com.sk.global.exception.BusinessException;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super(AuthErrorCode.USER_NOT_FOUND);
    }
}
