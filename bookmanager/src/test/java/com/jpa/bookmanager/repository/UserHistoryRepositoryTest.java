package com.jpa.bookmanager.repository;

import com.jpa.bookmanager.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.Table;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserHistoryRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    @Test
    void userHistoryTest(){
        User user = new User();
        user.setEmail("lisa22.@gmail.com");
        user.setName("lisa22");

        userRepository.save(user);

        user.setName("lisa22-new");
        userRepository.save(user);

        userHistoryRepository.findAll().forEach(System.out::println);
    }

}