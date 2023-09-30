package com.lucy.arti.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(BizException.class)
    public ResponseEntity<ErrorResult> bizException(BizException e) {
        return new ResponseEntity<>(ErrorResult.create(e.getBaseException()), e.getBaseException().getHttpStatus());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResult> authenticationException(AuthenticationException e) {
        return new ResponseEntity<>(new ErrorResult("AUTH_ERROR", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResult> accessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(new ErrorResult("ACCESS_DENIED", e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> notResolvedException(Exception e) {
        return new ResponseEntity<>(ErrorResult.create(InternalServerExceptionType.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> dtoValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[")
                    .append(fieldError.getField())
                    .append("](은)는 ")
                    .append(fieldError.getDefaultMessage())
                    .append(" 입력된 값: [")
                    .append(fieldError.getRejectedValue())
                    .append("]");
        }
        return new ResponseEntity<>(new ErrorResult("DTO_VALIDATION_ERROR", builder.toString()), HttpStatus.BAD_REQUEST);
    }
}
