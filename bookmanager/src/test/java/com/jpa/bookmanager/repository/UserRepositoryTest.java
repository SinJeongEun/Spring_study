package com.jpa.bookmanager.repository;

import com.jpa.bookmanager.domain.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;


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

    @Test
    void deleteInBatch(){
        //deleteAll()은 for loop를 통해 각각을 삭제하기 때문에, 성능이슈가 발생됨,,, 또한 delete 이전에 select 로 각 entity가 존재하는지 확인한다.
        //이를 해결하는 방법이 deleteAllInBatch 이다.
        //entity가 있던지 없던지 쿼리를 바로 던진다.(delete 이전에 select 하지 않는다.)

//        userRepository.deleteAll();
        userRepository.deleteAllInBatch();
        userRepository.findAll().forEach(System.out::println);
    }

    @Test
    void paging(){
        Page<User> users = userRepository.findAll(PageRequest.of(1,3)); // 한페이지에 3개씩 처리

        System.out.println("page : " + users);
        System.out.println("total elements : " + users.getTotalElements());
        System.out.println("total pages : " + users.getTotalPages());
        System.out.println("numberOfElements : "+ users.getNumberOfElements());
        System.out.println("size : " + users.getSize());
    }
    
    //query by example : entity를 example로 만들고, matcher를 선언하여, 필요한 쿼리들을 만드는 방법이다.
    @Test
    void qbe(){
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("name") // 무시할 속성
                .withMatcher("email", endsWith());

        Example<User> example = Example.of(new User("ma","nate.com"),matcher); // 이 객체는 probe로 실제 entityㄱ ㅏ아니다.
        //email 이 nate.come 으로 끝나느 항목을 찾는다.
        userRepository.findAll(example).forEach(System.out::println);
    }
}