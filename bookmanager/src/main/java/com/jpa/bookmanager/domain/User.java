package com.jpa.bookmanager.domain;
import com.jpa.bookmanager.domain.listener.UserEntityListener;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@Entity
@Table(name = "myUser")
@EntityListeners(value = {UserEntityListener.class})
public class User extends BaseEntity{
    @Id //User 테이블의 pk임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY)// 자동으로 증가하는 값이다.
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @OneToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "user_id", insertable = false, updatable = false) // userhistory는 read only 이여야 하기 때문에
    private List<UserHistory> userHistories = new ArrayList<>();

    @OneToMany
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private List<Review> reviews = new ArrayList<>();

}