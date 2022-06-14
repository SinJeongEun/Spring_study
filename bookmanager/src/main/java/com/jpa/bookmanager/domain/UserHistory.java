package com.jpa.bookmanager.domain;

import lombok.*;
import javax.persistence.*;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// 자동으로 증가하는 값이다.
    private Long id;

    private String name;

    private String email;

    @ManyToOne // 양방향 연결로, userHistory 에서도 user을 가져올 수 있다.
    private User user;
}