package com.example.exception_handler_ex.controller;

import com.example.exception_handler_ex.dto.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("api/user")
@Validated
public class RestApiController {

    @GetMapping // ?name=1234
    public User get(
            @Size(min = 2)
            @RequestParam String name,

            @NotNull
            @Min(1)
            @RequestParam Integer age){
        User user = new User();
        user.setName(name);
        user.setAge(age);

        return user;
    }


    @PostMapping
    public User post(@Valid @RequestBody User user){
        System.out.println(user);
        return user;
    }

//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e){
//        System.out.println("controller 내부에 있는 핸들러 동작 됨");
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//    }
}
