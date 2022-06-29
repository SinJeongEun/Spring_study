package com.jpa.bookmanager.domain;
import com.jpa.bookmanager.domain.converter.BookStatusConverter;
import com.jpa.bookmanager.domain.dto.BookStatus;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@Entity
@Where(clause = "deleted = false")
public class Book extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    private Long authorId;


    @OneToOne(mappedBy = "book")
    @ToString.Exclude
    private BookReviewInfo bookReviewInfo;

    @OneToMany
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}) // persist, merge에 대해 publisher에 영속성을 전이하겠다.
    @ToString.Exclude
    private Publisher publisher;

    @OneToMany
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    @Builder.Default
    private List<BookAndAuthor> bookAndAuthors = new ArrayList<>();

    private boolean deleted;

    @Convert(converter = BookStatusConverter.class) // 어떤 converter 와 연결되는지 명시한다.
    private BookStatus status; //판매 상태


    public void addBookAndAuthor(BookAndAuthor... bookAndAuthors){
        Collections.addAll(this.bookAndAuthors, bookAndAuthors);
    }
}
