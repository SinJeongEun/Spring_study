package com.jpa.bookmanager.repository;

import com.jpa.bookmanager.domain.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Test
    void queryMethod(){
        System.out.println("findByName : "+ userRepository.findByName("lisa"));
        System.out.println("findByEmail : "+ userRepository.findByEmail("martin@naver.com"));
        System.out.println("getByEmail : "+ userRepository.getByEmail("martin@naver.com"));
        System.out.println("readByEmail : "+ userRepository.readByEmail("martin@naver.com"));
        System.out.println("queryByEmail : "+ userRepository.queryByEmail("martin@naver.com"));
        System.out.println("searchByEmail : "+ userRepository.searchByEmail("martin@naver.com"));
        System.out.println("streamByEmail : "+ userRepository.streamByEmail("martin@naver.com"));

        System.out.println("findFirst1ByName : "+ userRepository.findFirst1ByName("martin"));
        System.out.println("findFirst2ByName : "+ userRepository.findFirst2ByName("martin")); //martin 의 이름을 가진 객체 2개를 가져온다. : limit 사용됨
        System.out.println("findTop1ByName : "+ userRepository.findTop1ByName("martin"));

        System.out.println("findByNameAndEmail : " + userRepository.findByNameAndEmail("martin","martin@naver.com"));
        System.out.println("findByEmailOrName : " + userRepository.findByEmailOrName("dennis","martin@naver.com"));
        System.out.println("findByCreateAtAfter : " + userRepository.findByCreateAtAfter(LocalDateTime.now().minusDays(1L)));
        System.out.println("findByIdAfter : " + userRepository.findByIdAfter(1L)); // before, after은 기준값을 포함하지 않는다.
        System.out.println("findByIdAfter : " + userRepository.findByIdAfter(1L));
        System.out.println("findByCreateAtGreaterThan : " + userRepository.findByCreateAtGreaterThan(LocalDateTime.now())); //after과 동일
        System.out.println("findByCreateAtGreaterThanEqual : " + userRepository.findByCreateAtGreaterThanEqual(LocalDateTime.now().minusDays(1L))); // where >= , 기준값 포함
        System.out.println("findByCreateAtBetween : " + userRepository.findByCreateAtBetween(LocalDateTime.now().minusDays(1L),LocalDateTime.now().plusDays(1L)));  // where >= , 기준값 포함
        System.out.println("findByIdBetween : " + userRepository.findByIdBetween(1L,3L));
        System.out.println("findByIdIsNotNull : " + userRepository.findByIdIsNull());
        System.out.println("findByNameIn : " + userRepository.findByNameIn(Lists.newArrayList("martin","lisa")));
        System.out.println("findByNameStartingWith : " + userRepository.findByNameStartingWith("ma"));
        System.out.println("findByNameEndingWith : " + userRepository.findByNameEndingWith("tin"));
        System.out.println("findByNameContains : " + userRepository.findByNameContains("rt"));
        System.out.println("findByNameLike : " + userRepository.findByNameLike("mar%"));
    }

    @Test
    void pagingAndSort(){
//        System.out.println("findTopByNameOrderByIdDesc : " + userRepository.findTopByNameOrderByIdDesc("martin"));
//        System.out.println("findTopByNameOrderByIdDescIdAsc : " + userRepository.findTopByNameOrderByIdDescIdAsc("martin"));
//        System.out.println("findFirstByName :  " + userRepository.findFirstByName("martin",Sort.by(Sort.Order.desc("id"))));
//        System.out.println("findFirstByName :  " + userRepository.findFirstByName("martin",Sort.by(Sort.Order.desc("id"), Sort.Order.asc("email"))));

        //0페이지는 첫번쨰 페이지를 나타낸다. 아래와 같이 정렬할 때 가장 먼저 나오는 것은 id=5의 martin이이므로 0페이지에는 id=5인 martin이 나온다.
        System.out.println("findByNameWithPaging : " + userRepository.findByName("martin",PageRequest.of(0,1,Sort.by(Sort.Order.desc("id")))).getContent());
        //1페이지는 id=1 인 martin이 나온다. .getContent()로  객체 내용을 볼 수 있다.
        System.out.println("findByNameWithPaging : " + userRepository.findByName("martin",PageRequest.of(1,1,Sort.by(Sort.Order.desc("id")))).getContent());

    }
}
