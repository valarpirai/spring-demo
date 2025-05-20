package com.example.demo.serivce;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    JdbcTemplate jdbcTemplate;

    @InjectMocks
    BookService bookService;

    @Test
    void test_getBookById() {
        Long bookId = 1L;
        Book book = new Book(bookId, "Test book", "Valar", LocalDate.now(), null);

        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM books WHERE id = ?"),
                any(RowMapper.class),
                eq(bookId))).thenReturn(book);

        // Act
        Book result = bookService.findBookById(bookId);

        // Assert
        assertThat(result.getTitle()).isEqualTo("Test book");

        // Capture the RowMapper argument
        ArgumentCaptor<RowMapper> rowMapperCaptor = ArgumentCaptor.forClass(RowMapper.class);
        verify(jdbcTemplate).queryForObject(
                eq("SELECT * FROM books WHERE id = ?"),
                rowMapperCaptor.capture(),
                eq(bookId)
        );
    }

}
