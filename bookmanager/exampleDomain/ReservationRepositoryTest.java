package com.jpa.bookmanager.exampleDomain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional
@SpringBootTest
class ReservationRepositoryTest {
    @Autowired
    private  ReservationRepository reservationRepository;

    @BeforeEach
    void before(){
        for (int i = 0; i < 10; i++) {
            Reservation reservation = Reservation.createReservation(LocalDate.now()); //2022-08-08
            reservationRepository.save(reservation);

        }
        for (int i = 0; i < 20; i++) {
            Reservation reservation = Reservation.createReservation(LocalDate.of(2022,8,7));
            reservationRepository.save(reservation);

        }
        for (int i = 0; i < 10; i++) {
            Reservation reservation = Reservation.createReservation(LocalDate.of(2022,8,5));

            reservationRepository.save(reservation);

        }

    }

    @Test
    void test1() { //100ms
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        reservationRepository.findAll()
                .stream()
                .forEach(r -> {
                    if(r.getDate().isBefore(LocalDate.now())) System.out.println(r);
                });
        stopWatch.stop();
        System.out.println("!! " + stopWatch.getTotalTimeMillis());
    }

    @Test
    void test2() { //145ms
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //where 조건으로 쿼리 한번에 실행 된다.
        reservationRepository.findByDateBefore(LocalDate.now()) // @Timer 사용함 쿼리 수행시간 :98
                .forEach(r-> System.out.println(r));
        stopWatch.stop();
        System.out.println("!! " + stopWatch.getTotalTimeMillis());
    }
}