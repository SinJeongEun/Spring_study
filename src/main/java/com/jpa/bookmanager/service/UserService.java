package com.jpa.bookmanager.service;

import com.jpa.bookmanager.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class UserService {
    private final EntityManager entityManager;

    @Transactional
    public void put(){
        User user = new User();
        user.setName("newUser");
        user.setEmail("newUser@naver.com");

        entityManager.persist(user); // save()와 같다. save() 함수 안에 이코드가 내제되어있다.
        entityManager.detach(user);

        user.setName("newUserAfterPersist");
        entityManager.merge(user);

        entityManager.clear();

    }
}
