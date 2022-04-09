package com.example.server.controller;

import com.example.server.dto.Req;
import com.example.server.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;


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

    //https://openapi.naver.com/v1/search/local.json?query=%EC%A3%BC%EC%8B%9D&display=10&start=1&sort=random
    @GetMapping("/naver")
    public String naver(){

        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("v1/search/local.json")
                .queryParam("query","갈비집")
                .queryParam("displqy",10)
                .queryParam("start",1)
                .queryParam("sort","random")
                .encode(Charset.forName("UTF-8"))
                .build()
                .toUri();


        RestTemplate restTemplate = new RestTemplate();

        RequestEntity<Void> requestEntitye = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id","uZ9bN_o47cRIM0LrP_qD")
                .header("X-Naver-Client-Secret","zklQDwlTZZ")
                .build();

        ResponseEntity<String> response = restTemplate.exchange(requestEntitye,String.class);

        return response.getBody();
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


