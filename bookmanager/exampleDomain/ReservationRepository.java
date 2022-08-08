package com.jpa.bookmanager.exampleDomain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Timer
    List<Reservation> findByDateBefore(LocalDate date);
}
