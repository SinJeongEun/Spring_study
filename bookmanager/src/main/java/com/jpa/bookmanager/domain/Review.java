package com.jpa.bookmanager.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Review extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private float score;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;


    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private List<Comment> comments;
}
