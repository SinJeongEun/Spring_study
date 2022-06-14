package com.jpa.bookmanager.repository;

import com.jpa.bookmanager.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.print.DocFlavor;

public interface ReviewRepository extends JpaRepository<Review,Long> {
}
