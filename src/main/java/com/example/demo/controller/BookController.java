package com.example.demo.controller;

import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.ReviewInputDto;
import com.example.demo.model.Book;
import com.example.demo.model.Review;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public PagedResponse<Book> getAllBooksWithReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return bookService.findAllBooksWithReviews(page, size);
    }

    @GetMapping("/book/{id}")
    Book getBookById(@PathVariable Long id) {
        return bookService.findBookWithReviews(id);
    }

    @PostMapping("/books")
    Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    @PostMapping("/book/{bookId}/review")
    public Review createReview(@PathVariable Long bookId, @RequestBody ReviewInputDto reviewDto) {
        Review review = new Review(null, reviewDto.comment(), reviewDto.rating(), null);
        return bookService.createReview(bookId, review);
    }

    @PutMapping("/book/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/book/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
