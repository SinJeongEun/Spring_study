package com.jpa.bookmanager.repository;

import com.jpa.bookmanager.domain.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void crud(){

        List<User>users1 = userRepository.findAll();
        List<User>users2 = userRepository.findAll(Sort.by(Sort.Direction.DESC,"name"));  // 이름의 내림차순 정렬
        List<User>users = userRepository.findAllById(Lists.newArrayList(1L,3L,5L)); // assert에서 제공하는 테스트 라이브러리를 통해 원하는 값들로 구성된 list 생성이 가능하다.
        users.forEach(System.out::println);

        User user1 = new User("jack","jack@naver.com");
        userRepository.save(user1);
    }

    @Test
    @Transactional  //session을 유지시켜 user 출력이 가능해진다.
    void getOne(){
        User user = userRepository.getReferenceById(1L);
        System.out.println(user);
    }

    @Test
    void findByID(){
        User user = userRepository.findById(1L).orElse(null); //findById는 optional 객체로 매핑된 객체를 return 해줌으로, 이와 같은 처리가 필요하다.
        System.out.println(user);
    }
}