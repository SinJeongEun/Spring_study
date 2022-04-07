package com.example.server.controller;

import com.example.server.dto.Req;
import com.example.server.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/server")
public class ServerApiController {

    @GetMapping("/hello")
    public User hello(@RequestParam String name, @RequestParam int age){
        User user = new User();
        user.setName(name);
        user.setAge(age);
        return user;
    }

    @PostMapping("/user/{userId}/name/{username}")
    public Req<User> post(@RequestBody Req<User> user,
                     @PathVariable int userId,
                     @PathVariable String username,
                     @RequestHeader("x-authorization") String authorization,
                     @RequestHeader("custom-header") String customHeader){
        log.info("user id : {}, userName;{}",userId,username);
        log.info("x-authorization : {}",authorization);
        log.info("custom-header : {}",customHeader);
        log.info("client req : {}",user);

        Req<User> response = new Req<>();
        response.setHeader(new Req.Header());
        response.setRBody(user.getRBody());
        return response;
    }
}


