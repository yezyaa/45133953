package com.sk.domain.board.exception;

import com.sk.global.exception.BusinessException;
import com.sk.global.exception.errorcode.BoardErrorCode;

public class BoardNotFoundException extends BusinessException {
    public BoardNotFoundException() {
        super(BoardErrorCode.BOARD_NOT_FOUND);
    }
}
