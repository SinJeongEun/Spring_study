package com.jpa.bookmanager.service;

import com.jpa.bookmanager.domain.Author;
import com.jpa.bookmanager.domain.Book;
import com.jpa.bookmanager.repository.AuthorRepository;
import com.jpa.bookmanager.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final AuthorService authorService;

    @Transactional(propagation = Propagation.REQUIRED)
    public void putBookAndAuthor() {
        Book book = new Book();
        book.setName("JPA 공부");

        bookRepository.save(book);

        try {
            authorService.putAuthor();
        }catch (RuntimeException e){

        }
//        Author author = new Author();
//        author.setName("lisa");
//        authorRepository.save(author);

        throw new RuntimeException("예외 발생으로 RollBack");
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void get(Long id){
        System.out.println(">>findById>> " + bookRepository.findById(id));
        System.out.println(">>findAll>> " + bookRepository.findAll());

        System.out.println(">>findById>> " + bookRepository.findById(id));
        System.out.println(">>findAll>> " + bookRepository.findAll());
        Book book = bookRepository.findById(id).get();
        book.setName("바뀔까?");
        bookRepository.save(book);
    }

    @Transactional
    public List<Book> getAll() {
        List<Book> books = bookRepository.findAll();

        books.forEach(System.out::println);

        return books;
    }
}
