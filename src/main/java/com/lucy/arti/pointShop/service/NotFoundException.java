package com.lucy.arti.pointShop.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{

    private final String code;

    public NotFoundException(String message) {
        super(message);
        this.code = "NOT_FOUND";
    }

    public String getCode() {
        return code;
    }
}
