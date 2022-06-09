package com.jpa.bookmanager.domain;


import com.jpa.bookmanager.domain.listener.Auditable;
import com.jpa.bookmanager.domain.listener.UserEntityListener;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@Entity
@Table(name = "myUser", indexes = {@Index(columnList = "name")}, uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
@EntityListeners(value = {UserEntityListener.class})
public class User extends BaseEntity implements Auditable {
    @Id //User 테이블의 pk임을 선언
    @GeneratedValue// 자동으로 증가하는 값이다.
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String email;

//    @Column(updatable = false)
//    @CreatedDate
//    private LocalDateTime createAt;
//
////    @Column(insertable = false)
//    @LastModifiedDate
//    private LocalDateTime updateAt;

    //entity listener
//    @PrePersist
//    public void prePersist(){
//        this.createAt = LocalDateTime.now();
//        this.updateAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    public void preUpdate(){
//        this.updateAt = LocalDateTime.now();
//    }
}
