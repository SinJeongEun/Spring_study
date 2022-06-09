package com.jpa.bookmanager.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void  test(){
        User user = new User();
        user.setEmail("lisa@naver.com");
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());

//        User user1 = new User(null,"lisa","lisa2@naver.com",LocalDateTime.now(),LocalDateTime.now());
        User user2 = new User("lisa3","lisa3@naver.com");

        User user3 = User.builder()
                .name("lisa4")
                .email("lisa42@naver.com")
                .build();

        System.out.println(">>>   "+ user.toString());
    }

}