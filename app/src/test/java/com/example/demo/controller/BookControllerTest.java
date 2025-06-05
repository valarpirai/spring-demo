package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void getAllBooksWithReviews() {
        // TODO: Implement this test
    }

    @Test
    void getBookById() throws Exception {
        // Arrange
        Long bookId = 1L;
        Book book = new Book(bookId, "Test book", "Valar", LocalDate.now(), 1L, null);

        when(bookService.findBookWithReviews(eq(bookId))).thenReturn(book);

        // Act & Assert
        mockMvc.perform(get("/book/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Test book"))
                .andExpect(jsonPath("$.author").value("Valar"));
    }

    @Test
    void createBook() {
        // TODO: Implement this test
    }
}