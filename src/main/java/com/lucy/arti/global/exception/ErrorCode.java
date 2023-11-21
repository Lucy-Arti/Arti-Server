package com.lucy.arti.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {



    MEMBER_DUPLICATE_EMAIL(1001, 400, "이미 가입된 사용자의 이메일입니다."),
    MEMBER_NICKNAME_LENGTH(1002, 400, "닉네임 길이는 10자 이하만 가능합니다."),
    MEMBER_DUPLICATE_NICKNAME(1003, 400, "이미 사용중인 닉네임이 존재합니다."),

    NOT_FOUND_REFRESH_TOKEN_IN_COOKIE(2001, 401, "엑세스 토큰 재발급을 위한 리프레시 토큰이 존재하지 않습니다."),
    NOT_FOUND_REFRESH_TOKEN_IN_REPOSITORY(2002, 401, "리프레시 토큰 저장소에 존재하지 않는 리프레시 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(2003, 401, "리프레시 토큰이 만료되었습니다"),
    AUTHORIZATION_FAIL(2004, 401, "엑세스 토큰이 존재하지 않거나 만료되었습니다."),

    CLOTHES_INVALID_TYPE(3001, 400, "유효한 옷 타입이 아닙니다."),
    CLOTHES_NO_NAME(3002, 400, "입력된 옷 이름이 없습니다."),
    CLOTHES_NO_TYPE(3003, 400, "입력된 옷 타입이 없습니다."),

    DESIGNER_NO_NAME(4001, 400, "디자이너의 이름이 없습니다."),
    DESIGNER_NOT_FOUND(4003, 400, "해당 id의 디자이너를 찾을 수 없습니다."),
    DESIGNER_LINK_ERROR(4002, 400, "url 주소의 형식이 잘못되었습니다.");





    private final int value;
    private final int httpStatusCode;
    private final String message;

    ErrorCode(int value, int httpStatusCode, String message) {
        this.value = value;
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }
}