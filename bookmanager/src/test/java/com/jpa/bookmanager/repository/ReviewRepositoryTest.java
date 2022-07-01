package com.jpa.bookmanager.repository;

import com.jpa.bookmanager.domain.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class ReviewRepositoryTest {
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @Transactional
    void reviewTest() {
        List<Review> reviews = reviewRepository.findAllByFetchJoin();
//        List<Review> reviews = reviewRepository.findAllByEntityGraph();
//        List<Review> reviews = reviewRepository.findAll();
        reviews.forEach(System.out::println);
//        System.out.println("전체를 가져옴");
//
//        System.out.println(reviews.get(0).getComments());
//        System.out.println("1번 리뷰");
//        System.out.println(reviews.get(1).getComments());
//        System.out.println("2번리뷰");






    }
}