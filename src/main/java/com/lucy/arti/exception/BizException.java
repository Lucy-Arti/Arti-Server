package com.lucy.arti.exception;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {
    private final BaseException baseException;

    public BizException(BaseException baseException) {
        super(baseException.getMessage());
        this.baseException = baseException;
    }
}
