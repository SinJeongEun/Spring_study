package com.jpa.bookmanager.repository;

import com.jpa.bookmanager.domain.Book;
import com.jpa.bookmanager.domain.dto.BookNameAndCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Modifying
    @Query(value = "update book set category='none'", nativeQuery = true)
    void update();

    List<Book> findByCategoryIsNull();

    List<Book> findByDeletedFalse();

    List<Book> findByCategoryIsNullAndDeletedFalse();

    @Query(value = "select b from Book b"
            + "where name = :name and createAt >= :createAt and updateAt >= :updateAt and category is null", nativeQuery = true)
    List<Book> findByNameRecently(@Param("name") String name,
                                  @Param("createAt") LocalDateTime createAt,
                                  @Param("updateAt") LocalDateTime updateAt);

    @Query(value = "select new com.jpa.bookmanager.domain.dto.BookNameAndCategory(b.name, b.category) from Book b")
    List<BookNameAndCategory> findBookNameAndCategory();

    @Query(value = "select new com.jpa.bookmanager.domain.dto.BookNameAndCategory(b.name, b.category) from Book b")
    Page<BookNameAndCategory> findBookNameAndCategory(Pageable pageable);

    @Query(value = "select * from book", nativeQuery = true)
    List<Book> findAllCustom();

    @Transactional
    @Modifying
    @Query(value = "update book set category = 'IT전문서'", nativeQuery = true)
    int updateCategories();
}
