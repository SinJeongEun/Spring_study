package com.jpa.bookmanager.domain.listener;

import com.jpa.bookmanager.domain.User;
import com.jpa.bookmanager.domain.UserHistory;
import com.jpa.bookmanager.repository.UserHistoryRepository;
import com.jpa.bookmanager.support.BeanUtils;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class UserEntityListener { //entity listener은 spring bean 을 주입받지 못한다.
    @PrePersist
    @PreUpdate
    public void prePersistAndPreUpdate(Object o){
        UserHistoryRepository userHistoryRepository = BeanUtils.getBean(UserHistoryRepository.class); // bean을 주입받는다.
        User user =(User) o;

        UserHistory userHistory = new UserHistory();
        userHistory.setUserId(user.getId());
        userHistory.setName(user.getName());
        userHistory.setEmail(user.getEmail());

        userHistoryRepository.save(userHistory);
    }
}
