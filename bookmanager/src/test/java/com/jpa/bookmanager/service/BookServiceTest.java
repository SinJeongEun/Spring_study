package com.jpa.bookmanager.service;

import com.jpa.bookmanager.domain.Book;
import com.jpa.bookmanager.repository.AuthorRepository;
import com.jpa.bookmanager.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceTest {
    @Autowired
    private  BookService bookService;
    @Autowired
    private  BookRepository bookRepository;
    @Autowired
    private  AuthorRepository authorRepository;

    @Test
    void putBookAndAuthor() {
        try {
            bookService.putBookAndAuthor();
        }catch (RuntimeException e) {
            System.out.println(">>> " + e.getMessage());
        }
        System.out.println("books : "+ bookRepository.findAll());
        System.out.println("authors : "+ authorRepository.findAll());
    }

    @Test
    void get() {
        Book book = new Book();
        book.setName("JPA 공부");

        bookRepository.save(book);

        bookService.get(1L);
        System.out.println(">>>>   " + bookRepository.findAll());
    }
}