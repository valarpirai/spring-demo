package com.example.demo.service;

import com.example.demo.dto.PagedResponse;
import com.example.demo.eventlisteners.MyCustomEvent;
import com.example.demo.model.Book;
import com.example.demo.model.Review;
import com.example.demo.model.ReviewWithBookId;
import com.example.demo.repository.BookRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class BookService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JdbcTemplate jdbcTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private final BookRepository bookRepository;

    // RowMapper for Book
    private final RowMapper<Book> bookRowMapper =
            (rs, rowNum) ->
                    new Book(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getObject("publication_date", LocalDate.class),
                            (Long) rs.getObject("version"),
                            new ArrayList<>());

    // RowMapper for Review
    private final RowMapper<ReviewWithBookId> reviewRowMapper =
            (rs, rowNum) -> {
                Review review =
                        new Review(
                                rs.getLong("id"),
                                rs.getString("comment"),
                                rs.getInt("rating"),
                                null // Book reference set later
                                );
                Long bookId = rs.getLong("book_id");
                return new ReviewWithBookId(review, bookId);
            };

    public BookService(
            JdbcTemplate jdbcTemplate,
            ApplicationEventPublisher eventPublisher,
            BookRepository bookRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventPublisher = eventPublisher;
        this.bookRepository = bookRepository;
    }

    // Create a Book
    @Transactional
    public Book createBook(Book book) {
        String sql =
                "INSERT INTO books (title, author, publication_date, version) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(
                sql,
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationDate(),
                book.getVersion());

        // Retrieve generated ID
        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        eventPublisher.publishEvent(new MyCustomEvent(this, "Transaction completed!"));
        return new Book(
                id,
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationDate(),
                1L,
                new ArrayList<>());
    }

    // Create a Review and associate it with a Book
    @Transactional
    public Review createReview(Long bookId, Review review) {
        // Verify book exists
        String checkSql = "SELECT COUNT(*) FROM books WHERE id = ?";
        Long count = jdbcTemplate.queryForObject(checkSql, Long.class, bookId);
        if (count == null || count == 0) {
            throw new IllegalArgumentException("Book with ID " + bookId + " does not exist");
        }

        String sql = "INSERT INTO reviews (comment, rating, book_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, review.getComment(), review.getRating(), bookId);

        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        return new Review(id, review.getComment(), review.getRating(), null);
    }

    // Read a Book by ID
    public Book findBookById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        Book book = jdbcTemplate.queryForObject(sql, bookRowMapper, id);
        if (book == null) {
            throw new RuntimeException("Book not found");
        }
        return book;
    }

    @Transactional
    public Book findBookWithReviews(Long bookId) {
        Book book = bookRepository.findWithLockingById(bookId).orElseThrow();
        Hibernate.initialize(book.getReviews()); // Force initialization
        return book;
    }

    // Update a Book
    public Book updateBook(Long id, Book updatedBook) {
        String sql = "UPDATE books SET title = ?, author = ?, publication_date = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                updatedBook.getTitle(),
                updatedBook.getAuthor(),
                updatedBook.getPublicationDate(),
                id);
        return new Book(
                id,
                updatedBook.getTitle(),
                updatedBook.getAuthor(),
                updatedBook.getPublicationDate(),
                1L,
                new ArrayList<>());
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
            String reviewSql =
                    "SELECT * FROM reviews WHERE book_id IN ("
                            + books.stream()
                                    .map(book -> book.getId().toString())
                                    .collect(Collectors.joining(","))
                            + ")";
            List<ReviewWithBookId> reviewWithBookIds =
                    jdbcTemplate.query(reviewSql, reviewRowMapper);

            // Group Reviews by book_id
            Map<Long, List<Review>> reviewsByBookId =
                    reviewWithBookIds.stream()
                            .collect(
                                    Collectors.groupingBy(
                                            ReviewWithBookId::bookId,
                                            Collectors.mapping(
                                                    ReviewWithBookId::review,
                                                    Collectors.toList())));

            // Associate Reviews with Books
            books.forEach(
                    book -> {
                        List<Review> bookReviews =
                                reviewsByBookId.getOrDefault(book.getId(), new ArrayList<>());
                        bookReviews.forEach(
                                review -> {
                                    Review updatedReview =
                                            new Review(
                                                    review.getId(),
                                                    review.getComment(),
                                                    review.getRating(),
                                                    null);
                                    updatedReview.setBook(book);
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
