package com.example.interceptor.controller;

import com.example.interceptor.annotation.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/private")
@Auth  //interceptor에서 컨트롤러에 @Auth 어노테이션이 붙어있으면 세션을 검사하여 세션이 있을경우에만 통과시킨다.
public class PrivateController {
    //세션이 인증 된 사용자에게만 리턴
    @GetMapping("/hello")
    public String hello(){
        log.info("controller 진입~");
        return "private hello";
    }
}
