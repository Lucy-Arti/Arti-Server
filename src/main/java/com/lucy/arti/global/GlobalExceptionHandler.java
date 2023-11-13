package com.lucy.arti.global;

import com.lucy.arti.global.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 실패 응답을 처리
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<Void> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.notFound().build(); // ResponseEntity.notFound()
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build(); // ResponseEntity.badRequest() 400 에러코드
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Void> handleBuisinessException(BusinessException e) {
        return ResponseEntity.internalServerError().build();
    }
}
