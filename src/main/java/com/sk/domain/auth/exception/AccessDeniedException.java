package com.sk.domain.auth.exception;

import com.sk.global.exception.BusinessException;

public class AccessDeniedException extends BusinessException {
    public AccessDeniedException() {
        super(AuthErrorCode.ACCESS_DENIED);
    }
}
