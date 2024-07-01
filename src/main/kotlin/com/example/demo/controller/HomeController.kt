package com.example.demo.controller

import com.example.demo.dto.AuthorDto
import com.example.demo.dto.BookDto
import com.example.demo.model.Book
import com.example.demo.service.BookService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
    fun authors(): MutableList<AuthorDto> {
        return bookService.getAllAuthorsWithBookDetails()
    }

    @GetMapping("/books")
    fun books(): MutableList<BookDto> {
        return bookService.getAllBooks()
    }

    @PostMapping("/book")
    fun createBook(@RequestBody book: Book): Any {
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
