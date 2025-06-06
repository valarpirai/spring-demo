package com.example.demo.repository;

import com.example.demo.model.Review;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findByBookId(Long bookId);

    List<Review> findByRating(Integer rating);

    Page<Review> findByBookId(Long bookId, Pageable pageable);

    List<Review> findByRatingBetween(Integer minRating, Integer maxRating);

    List<Review> findTop10ByOrderByIdDesc();
}
