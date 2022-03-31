package com.example.exception_handler_ex.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
//@RestControllerAdvice(basePackages = "com.example.exception_handler_ex.controller") //적용시킬 부분 직접 명시 가능, 생략 시 글로벌하게 적용 됨
public class GlobalControllerAdvice {

    //rest api 는 responseEntity를 밷는다.
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity exception (Exception e){
        System.out.println("---------------------" + e.getClass().getName());
        System.out.println(e.getLocalizedMessage());
        System.out.println("---------------------");


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("실패");
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e){
        System.out.println("MethodArgumentNotValidException 전용 핸들러 동작");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
