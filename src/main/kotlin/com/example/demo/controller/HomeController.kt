package com.example.demo.controller

import com.example.demo.aop.LoggerAspect
import com.example.demo.dto.AuthorDto
import com.example.demo.dto.BookDto
import com.example.demo.model.Author
import com.example.demo.model.Book
import com.example.demo.repository.AuthorRepository
import com.example.demo.repository.BookRepository
import com.example.demo.service.BookService
import org.modelmapper.ModelMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.stream.Collectors
import kotlin.reflect.KFunction1

@RestController
class HomeController {
    private val logger: Logger = LoggerFactory.getLogger(HomeController::class.java)

    @Autowired
    lateinit var bookService: BookService

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

}
