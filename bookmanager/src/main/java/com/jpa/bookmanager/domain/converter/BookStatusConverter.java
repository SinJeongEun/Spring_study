package com.jpa.bookmanager.domain.converter;

import com.jpa.bookmanager.domain.dto.BookStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class BookStatusConverter implements AttributeConverter<BookStatus, Integer> {

    @Override
    // 자바 객체 -> db
    public Integer convertToDatabaseColumn(BookStatus attribute) {
        return attribute.getCode();
//        return null;
    }

    @Override
    // db -> 자바 객체
    // nullPointException이 일어나면 절때 안된다. DB에 접근하는 Data이기 때문에 민감하기때문
    public BookStatus convertToEntityAttribute(Integer dbData) {
        return dbData != null ? new BookStatus(dbData) : null;
    }
}
