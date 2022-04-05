package com.example.interceptor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
//모든 사용자가 접근 가능한 controller
public class PublicController {
    @GetMapping("/hello")
    public String hello(){
        return "public hello";
    }
}
