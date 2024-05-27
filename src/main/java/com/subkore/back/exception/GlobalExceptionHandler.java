package com.subkore.back.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // TODO: 예외처리 고도화 필요. 예외처리용 클래스 생성 필요.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(CarouselException.class)
    public ResponseEntity<String> handleCarouselException(CarouselException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(MenuException.class)
    public ResponseEntity<String> handleMenuException(MenuException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
