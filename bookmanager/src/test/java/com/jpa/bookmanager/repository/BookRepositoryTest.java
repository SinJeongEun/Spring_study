package com.jpa.bookmanager.repository;

import com.jpa.bookmanager.domain.Book;
import com.jpa.bookmanager.domain.Publisher;
import com.jpa.bookmanager.domain.Review;
import com.jpa.bookmanager.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    void bookTest(){
        Book book = new Book();
        book.setName("jpa 실습");
        book.setAuthorId(1L);
//        book .setPublisherId(1L);

        bookRepository.save(book);
        System.out.println(bookRepository.findAll());
    }

    @Test
    @Transactional
    void bookRelationTest(){
        givenBookAndReview();

        User user = userRepository.findByEmail("martin@naver.com");

        System.out.println("Review : " + user.getReviews());
        System.out.println("Book : " + user.getReviews().get(0).getBook());
        System.out.println("Publisher : " + user.getReviews().get(0).getBook().getPublisher());
    }

    private void givenBookAndReview(){
        givenReveiw(givenUser(),givenBook(givenPublisher()));
    }

    private User givenUser(){
        return userRepository.findByEmail("martin@naver.com");
    }

    private Book givenBook(Publisher publisher){
        Book book = new Book();
        book.setName("jpa 책");
        book.setPublisher(publisher);

        return bookRepository.save(book);
    }

    private Publisher givenPublisher(){
        Publisher publisher = new Publisher();
        publisher.setName("신정은출판사");

        return publisherRepository.save(publisher);
    }
    private void givenReveiw(User user, Book book){
        Review review = new Review();
        review.setTitle("내 삶을 바꾼 책");
        review.setContent("유익한  책이에여");
        review.setScore(4.5f);
        review.setUser(user);
        review.setBook(book);

        reviewRepository.save(review);
    }
}