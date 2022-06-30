package com.jpa.bookmanager.repository;

import com.jpa.bookmanager.domain.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.print.DocFlavor;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    @Query("select distinct r from Review r join fetch r.comments")
    List<Review> findAllByFetchJoin();

    @EntityGraph(attributePaths = "comments")
    @Query("select r from Review r")
    List<Review> findAllByEntityGraph();
}
