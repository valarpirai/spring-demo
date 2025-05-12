package com.example.demo.repository;

import com.example.demo.model.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findByBookId(Long bookId);
    List<Review> findByRating(Integer rating);
    Page<Review> findByBookId(Long bookId, Pageable pageable);
    List<Review> findByRatingBetween(Integer minRating, Integer maxRating);
    List<Review> findTop10ByOrderByIdDesc();
}
