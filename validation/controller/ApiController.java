package com.example.validation.controller;

import com.example.validation.dto.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class ApiController {

    @PostMapping("/user")
    public ResponseEntity user (@Valid @RequestBody User user, BindingResult bindingResult){
        // valid 어노테이션을 붙은 는 그 안의 어노테이션을 ㅇ검사하게 된다
        // 지금처럼 에러가 터지는게 아니라 validation에 대한 결과자 BindingResult 에 값이 들어 감
        // 예외처리로도 가능
        System.out.println(user);

        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            bindingResult.getAllErrors().forEach( objectError -> {
                FieldError field = (FieldError) objectError;
                String message = objectError.getDefaultMessage();

                System.out.println(field.getField() + "    " + message);

                sb.append(field.getField() + "\n");
                sb.append(message + "\n");
            });

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        return ResponseEntity.ok(user);
    }
}
