package com.jpa.bookmanager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable // entity 내부에 선언 가능하게 된다.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String city; //시

    private String district; // 구

    @Column(name = "address_detail")
    private String detail;  // 상세주소

    private String zipCode; // 우편번호
}
