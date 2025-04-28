package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public record Book(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id,

        @Column(nullable = false)
        String title,

        @Column(nullable = false)
        String author,

        @Column(name = "publication_date")
        LocalDate publicationDate,

        @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JsonManagedReference
        List<Review> reviews
) {
    // Default constructor required by JPA
    public Book {
        reviews = new ArrayList<>();
    }

    // Helper method to add a review
    public void addReview(Review review) {
        reviews.add(review);
    }
}
