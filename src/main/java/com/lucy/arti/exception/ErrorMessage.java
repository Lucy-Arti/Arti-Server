package com.lucy.arti.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    NOT_EXIST_USER("1001", "사용자 조회할 수 없음"),
    NOT_EXIST_CLOTHE("1002", "옷 조회할 수 없음"),
    ;

    private final String code;
    private final String reason;
}
