package com.jpa.bookmanager.service;

import com.jpa.bookmanager.domain.User;
import com.jpa.bookmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class EntityManagerTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void entityManagerTest(){
        // 이는 userRepository.findAll()과 같은 쿼리를 동작시킨다.
        System.out.println(entityManager.createQuery("select u from User u").getResultList());
    }

    @Test
    void cacheFindTest(){
        System.out.println(userRepository.findById(1L));
        System.out.println(userRepository.findById(1L));
        System.out.println(userRepository.findById(1L));
    }

    @Test
    void cacheFindTest2() {
        User user = userRepository.findById(1L).orElseThrow();
        user.setName("marrrrrrrtin");
        userRepository.save(user);

        System.out.println("---------------------");

        user.setEmail("marrrrrrtin@fast.com");
        userRepository.save(user);
        userRepository.flush();
    }
}
