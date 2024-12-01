package com.yezyaa.sk.domain.auth.exception;

import com.yezyaa.sk.global.exception.BusinessException;
import com.yezyaa.sk.global.exception.errorcode.AuthErrorCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super(AuthErrorCode.USER_NOT_FOUND);
    }
}
