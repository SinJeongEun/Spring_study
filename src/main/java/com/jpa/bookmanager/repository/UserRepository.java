package com.jpa.bookmanager.repository;

import com.jpa.bookmanager.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<User,Long> {

    //query method
    Set<User> findByName(String name);

    User findByEmail(String email);

    User getByEmail(String email);

    User readByEmail(String email);

    User queryByEmail(String email);

    User searchByEmail(String eamil);

    User streamByEmail(String email);

    User findFirst1ByName(String name);

    List<User> findFirst2ByName(String name);

    User findTop1ByName(String name);

    List<User> findByNameAndEmail(String name, String email);

    List<User> findByEmailOrName(String email, String name);

    List<User> findByCreateAtAfter(LocalDateTime yesterday);

    List<User> findByIdAfter(Long id);

    List<User> findByCreateAtGreaterThan(LocalDateTime yesterday);

    List<User> findByCreateAtGreaterThanEqual(LocalDateTime yesterday);

    List<User> findByCreateAtBetween(LocalDateTime yesterday, LocalDateTime tomorrow);

    List<User> findByIdBetween(Long id1, Long id2);

    List<User> findByIdIsNull();

    List<User> findByNameIn(List<String> name);

    List<User> findByNameStartingWith(String str);

    List<User> findByNameEndingWith(String str);

    List<User> findByNameContains(String str);

    List<User> findByNameLike(String str);

    List<User> findTopByNameOrderByIdDesc(String name);

    List<User> findTopByNameOrderByIdDescIdAsc(String name);

    List<User> findFirstByName(String name, Sort sort);

    Page<User> findByName(String name, Pageable pageable);


}
