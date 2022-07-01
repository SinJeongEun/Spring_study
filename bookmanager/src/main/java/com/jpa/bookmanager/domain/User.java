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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "home_city")),
            @AttributeOverride(name = "district", column = @Column(name = "home_district")),
            @AttributeOverride(name = "detail", column = @Column(name = "home_detail")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "home_zipCode"))
    })
    private Address homeAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "company_city")),
            @AttributeOverride(name = "district", column = @Column(name = "company_district")),
            @AttributeOverride(name = "detail", column = @Column(name = "company_detail")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "company_zipCode"))
    })
    private Address companyAddress;

    @OneToMany
    @ToString.Exclude
    @JoinColumn(name = "user_id", insertable = false, updatable = false) // userhistory는 read only 이여야 하기 때문에
    @Builder.Default
    private List<UserHistory> userHistories = new ArrayList<>();

    @OneToMany
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

}