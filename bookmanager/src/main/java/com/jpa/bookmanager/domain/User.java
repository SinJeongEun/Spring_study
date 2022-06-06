package com.jpa.bookmanager.domain;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
@Entity
@Table(name = "myUser")
public class User {
    @Id //User 테이블의 pk임을 선언
    @GeneratedValue// 자동으로 증가하는 값이다.
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String email;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;


}