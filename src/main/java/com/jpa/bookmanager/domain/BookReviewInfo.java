package com.jpa.bookmanager.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BookReviewInfo extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false) // null을 허용하지 않겠다는 의미
    private Book book;

    private float averageReviewScore;  //primitive 타입으로 자동으로 not null 조건으로 컬럼이 생성된다. wrpper 클래스는 null 혀용

    private int reviewCount;
}
