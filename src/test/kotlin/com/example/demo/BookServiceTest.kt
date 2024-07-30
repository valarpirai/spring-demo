package com.example.demo

import com.example.demo.repository.BookRepository
import com.example.demo.service.BookService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertNull

@ExtendWith(MockitoExtension::class)
class BookServiceTest {

    @Mock
    lateinit var bookRepo: BookRepository;

    @InjectMocks
    var bookService = BookService()

    @Test
    fun testGetBookById() {
        Mockito.`when`(bookRepo.findByIdAndPageCount(1, 720)).thenReturn(null)

        val book = bookService.getBookById(1);
        assertNull(book)
    }
}