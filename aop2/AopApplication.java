package com.example.aop2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SpringBootApplication
public class AopApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopApplication.class, args);
        System.out.println(Base64.getEncoder().encodeToString("steve@gmail.com".getBytes(StandardCharsets.UTF_8)));
    }

}
