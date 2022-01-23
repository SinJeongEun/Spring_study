package com.example.aop2.controller;

import com.example.aop2.annotation.Decode;
import com.example.aop2.annotation.Timer;
import com.example.aop2.dto.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RestApiController {

    @GetMapping("/get/{id}")
    public String get(@PathVariable Long id, @RequestParam String name){
//        System.out.println("get method");
//        System.out.println("get method : " + id);
//        System.out.println("get method : " + name );
        return id + " " + name;
    }

    @PostMapping("/post")
    public User post(@RequestBody User user){
//        System.out.println("post method : " + user);
        return user;
    }

    @Timer
    @DeleteMapping
    public void delete() throws InterruptedException {
        // db logic
        Thread.sleep(1000 * 2);
    }

    @Decode
    @PutMapping ("/put")
    public User put(@RequestBody User user){
        System.out.println("put");
        System.out.println(user);
        return user;
    }
}
