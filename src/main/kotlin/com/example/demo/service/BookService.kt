package com.example.demo.service

import com.example.demo.controller.HomeController
import com.example.demo.dto.AuthorDto
import com.example.demo.dto.BookDto
import com.example.demo.model.Author
import com.example.demo.model.Book
import com.example.demo.repository.AuthorRepository
import com.example.demo.repository.BookRepository
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.instrumentation.annotations.WithSpan
import org.modelmapper.ModelMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BookService {
    private val logger: Logger = LoggerFactory.getLogger(BookService::class.java)

    @Autowired
    lateinit var bookRepository: BookRepository;

    @Autowired
    lateinit var authorRepository: AuthorRepository;

    @Autowired
    lateinit var modelMapper: ModelMapper;

    @WithSpan(value = "authorDTO", kind = SpanKind.SERVER)
    private fun convertToDto(author: Author): AuthorDto {
        return modelMapper.map(author, AuthorDto::class.java);
    }

    @WithSpan(value = "greeting")
    private fun convertToDto(book: Book): BookDto {
        return modelMapper.map(book, BookDto::class.java);
    }

    @WithSpan
    fun createBook(book: Book): Any {
        val author = book.author;

        val auth = author?.firstName?.let { author.lastName?.let { it1 ->
            authorRepository.findByFirstNameAndLastName(it,
                it1
            )
        } }

        if (auth != null) {
            // Use Existing author
            book.author = auth
        } else {
           // Create Author
            book.author = authorRepository.save(book.author!!);
        }
        return convertToDto(bookRepository.save(book));
    }

    @WithSpan
    fun getBookById(bookId: Long): BookDto? {
        val optionalBook = bookRepository.findById(bookId)
        if (optionalBook.isPresent) {
            return convertToDto(optionalBook.get())
        }
        return null;
    }

    @WithSpan
    fun getAllBooks(): MutableList<BookDto> {
        val books = mutableListOf<BookDto>()
        for (book in bookRepository.findAll().toList()) {
            books.add(convertToDto(book))
        }
        return books;
    }

    @WithSpan
    fun getAllAuthorsWithBookDetails(): MutableList<AuthorDto> {
        val authors = authorRepository.findAll().toList();
        logger.info(authors.count().toString())
        //        return authors.stream().map { this::convertToDto }.collect(Collectors.toList())
        val authorDtos = mutableListOf<AuthorDto>()

        for (author in authors) {
            authorDtos.add(convertToDto(author))
        }
        return authorDtos;
    }

    @WithSpan
    fun getAuthorById(authorId: Long): Author {
        return authorRepository.findById(authorId).get()
    }
}