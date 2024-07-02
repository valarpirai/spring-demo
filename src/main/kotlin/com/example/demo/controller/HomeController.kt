package com.example.demo.controller

import com.example.demo.dto.AuthorDto
import com.example.demo.dto.AuthorWithBooksDto
import com.example.demo.dto.BookDto
import com.example.demo.dto.BookWithAuthorDto
import com.example.demo.model.Book
import com.example.demo.service.BookService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.util.*

@RestController
class HomeController {
    private val logger: Logger = LoggerFactory.getLogger(HomeController::class.java)

    @Autowired
    lateinit var bookService: BookService

    @Autowired
    lateinit var restTemplate: RestTemplate

    @GetMapping("/")
    fun home() : String {
        return "Hello World";
    }

    @GetMapping("/authors")
    fun authors(): MutableList<AuthorWithBooksDto> {
        return bookService.getAllAuthorsWithBookDetails()
    }

    @GetMapping("/books")
    fun books(@RequestParam(required = false, defaultValue = "0") page: Int, @RequestParam(required = false, defaultValue = "5") pageSize: Int): MutableList<BookWithAuthorDto> {
        return bookService.getBooks(page, pageSize);
    }

    @GetMapping("/booksWithCursor")
    fun booksWithCursor(@RequestParam(required = false, defaultValue = "10") first: Int, @RequestParam(required = false, defaultValue = "") cursor: String): Map<String, Any> {
        return bookService.getBooksCursor(first, cursor)
    }

    @PostMapping("/book")
    fun createBook(@RequestBody book: Book): BookWithAuthorDto? {
        return bookService.createBook(book);
    }

    @GetMapping("/httpbin")
    fun callHttpBin(): ResponseEntity<String> {
        val response = restTemplate.getForEntity(
            "https://httpbin.org/get", String::class.java
        )
        return response;
    }

    @GetMapping("/error")
    fun error() {
        throwError()
    }

    fun throwError() {
        throw Exception("Test Exceptions");
    }

}
