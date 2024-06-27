package com.example.demo.controller

import com.example.demo.aop.LoggerAspect
import com.example.demo.dto.AuthorDto
import com.example.demo.dto.BookDto
import com.example.demo.model.Author
import com.example.demo.model.Book
import com.example.demo.repository.AuthorRepository
import com.example.demo.repository.BookRepository
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
    lateinit var bookRepository: BookRepository;

    @Autowired
    lateinit var authorRepository: AuthorRepository;

    @Autowired
    lateinit var modelMapper: ModelMapper;

    @GetMapping("/")
    fun home() : String {
        return "Hello World";
    }

    @GetMapping("/authors")
    fun authors(): MutableList<AuthorDto> {
        val authors = authorRepository.findAll().toList();
        logger.info(authors.count().toString())
//        return authors.stream().map { this::convertToDto }.collect(Collectors.toList())
        val authorDtos = mutableListOf<AuthorDto>()

        for (author in authors) {
            authorDtos.add(convertToDto(author))
        }
        return authorDtos;
    }

    @GetMapping("/books")
    fun books(): MutableList<BookDto> {
        val books = mutableListOf<BookDto>()
        for (book in bookRepository.findAll().toList()) {
            books.add(convertToDto(book))
        }
        return books;
    }

    @PostMapping("/book")
    fun createBook(@RequestBody book: Book): Any {
        val author = book.author;
        var auth: Author? = null;

        auth = author?.firstName?.let { author?.lastName?.let { it1 ->
            authorRepository.findByFirstNameAndLastName(it,
                it1
            )
        } }

        if (auth != null) {
//            Use Existing author
            book.author = auth
        } else {
//            Create Author
            book.author = authorRepository.save(book.author!!);
        }
        return convertToDto(bookRepository.save(book));
    }

    private fun convertToDto(author: Author): AuthorDto {
        return modelMapper.map(author, AuthorDto::class.java);
    }

    private fun convertToDto(book: Book): BookDto {
        return modelMapper.map(book, BookDto::class.java);
    }
}
