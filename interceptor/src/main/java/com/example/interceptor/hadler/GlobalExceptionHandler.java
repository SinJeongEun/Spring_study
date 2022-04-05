package com.example.interceptor.hadler;

import com.example.interceptor.exception.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity authException(){
        System.out.println("AuthException handler 작동");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //401 error 발생
    }
}
