package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private BookService bookService;

    @Test
    void getAllBooksWithReviews() {
        var result = bookService.findAllBooksWithReviews(1, 10);
        assertNull(result);
    }

    @Test
    void getBookById() throws Exception {
        // Arrange
        Long bookId = 1L;
        Book book = new Book(bookId, "Test book", "Valar", LocalDate.now(), 1L, null);

        when(bookService.findBookWithReviews(bookId)).thenReturn(book);

        // Act & Assert
        mockMvc.perform(get("/book/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Test book"))
                .andExpect(jsonPath("$.author").value("Valar"));
    }
}
