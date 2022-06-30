package com.jpa.bookmanager.domain;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Comment extends Serializers.Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String comment;

    @ManyToOne
    @ToString.Exclude
    private Review review;

}
