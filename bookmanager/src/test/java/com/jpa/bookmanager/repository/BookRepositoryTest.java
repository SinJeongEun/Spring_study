package com.jpa.bookmanager.repository;

import com.jpa.bookmanager.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    void bookTest(){
        Book book = new Book();
        book.setName("jpa 실습");
        book.setAuthor("신정은");

        bookRepository.save(book);
        System.out.println(bookRepository.findAll());
    }
}