package com.example.demo.service;

import com.example.demo.dto.PagedResponse;
import com.example.demo.model.Book;
import com.example.demo.model.Review;
import com.example.demo.model.ReviewWithBookId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper for Book
    private final RowMapper<Book> bookRowMapper = (rs, rowNum) -> new Book(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getObject("publication_date", LocalDate.class),
            new ArrayList<>()
    );

    // RowMapper for Review
    private final RowMapper<ReviewWithBookId> reviewRowMapper = (rs, rowNum) -> {
        Review review = new Review(
                rs.getLong("id"),
                rs.getString("comment"),
                rs.getInt("rating"),
                null // Book reference set later
        );
        Long bookId = rs.getLong("book_id");
        return new ReviewWithBookId(review, bookId);
    };

    // Create a Book
    @Transactional
    public Book createBook(Book book) {
        String sql = "INSERT INTO books (title, author, publication_date) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, book.title(), book.author(), book.publicationDate());

        // Retrieve generated ID
        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        return new Book(id, book.title(), book.author(), book.publicationDate(), new ArrayList<>());
    }

    // Create a Review and associate it with a Book
    @Transactional
    public Review createReview(Long bookId, Review review) {
        // Verify book exists
        String checkSql = "SELECT COUNT(*) FROM books WHERE id = ?";
        Long count = jdbcTemplate.queryForObject(checkSql, Long.class, bookId);
        if (count == 0) {
            throw new IllegalArgumentException("Book with ID " + bookId + " does not exist");
        }

        String sql = "INSERT INTO reviews (comment, rating, book_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, review.comment(), review.rating(), bookId);

        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        return new Review(id, review.comment(), review.rating(), null);
    }

    // Read a Book by ID
    public Book findBookById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, bookRowMapper, id);
    }

    // Read a Book with its Reviews (join query)
    public Book findBookWithReviews(Long bookId) {
        // Fetch Book
        String bookSql = "SELECT * FROM books WHERE id = ?";
        Book book = jdbcTemplate.queryForObject(bookSql, bookRowMapper, bookId);

        // Fetch Reviews
        String reviewSql = "SELECT * FROM reviews WHERE book_id = ?";
        List<ReviewWithBookId> reviewWithBookIds = jdbcTemplate.query(reviewSql, reviewRowMapper, bookId);

        // Extract Reviews and set Book reference
        reviewWithBookIds.forEach(wrapper -> {
            Review review = wrapper.review();
            Review updatedReview = new Review(review.id(), review.comment(), review.rating(), book);
            book.addReview(updatedReview);
        });

        return book;
    }

    // Update a Book
    public Book updateBook(Long id, Book updatedBook) {
        String sql = "UPDATE books SET title = ?, author = ?, publication_date = ? WHERE id = ?";
        jdbcTemplate.update(sql, updatedBook.title(), updatedBook.author(), updatedBook.publicationDate(), id);
        return new Book(id, updatedBook.title(), updatedBook.author(), updatedBook.publicationDate(), new ArrayList<>());
    }

    // Delete a Book (and its Reviews due to cascade)
    @Transactional
    public void deleteBook(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Read all Books with their Reviews
    // Read all Books with their Reviews with pagination
    public PagedResponse<Book> findAllBooksWithReviews(int page, int size) {
        // Validate pagination parameters
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page must be >= 0 and size must be > 0");
        }

        if (size > 100) {
            size = 100;
        }

        // Calculate offset
        int offset = page * size;

        // Count total books
        String countSql = "SELECT COUNT(*) FROM books";
        long totalElements = jdbcTemplate.queryForObject(countSql, Long.class);

        // Fetch paginated Books
        String bookSql = "SELECT * FROM books ORDER BY id LIMIT ? OFFSET ?";
        List<Book> books = jdbcTemplate.query(bookSql, bookRowMapper, size, offset);

        // Fetch all Reviews for the fetched Books
        if (!books.isEmpty()) {
            String reviewSql = "SELECT * FROM reviews WHERE book_id IN (" +
                    books.stream().map(book -> book.id().toString()).collect(Collectors.joining(",")) + ")";
            List<ReviewWithBookId> reviewWithBookIds = jdbcTemplate.query(reviewSql, reviewRowMapper);

            // Group Reviews by book_id
            Map<Long, List<Review>> reviewsByBookId = reviewWithBookIds.stream()
                    .collect(Collectors.groupingBy(
                            ReviewWithBookId::bookId,
                            Collectors.mapping(ReviewWithBookId::review, Collectors.toList())
                    ));

            // Associate Reviews with Books
            books.forEach(book -> {
                List<Review> bookReviews = reviewsByBookId.getOrDefault(book.id(), new ArrayList<>());
                bookReviews.forEach(review -> {
                    Review updatedReview = new Review(review.id(), review.comment(), review.rating(), book);
                    book.addReview(updatedReview);
                });
            });
        }

        // Calculate total pages
        int totalPages = (int) Math.ceil((double) totalElements / size);

        // Return paginated response
        return new PagedResponse<>(books, page, size, totalElements, totalPages);
    }
}
