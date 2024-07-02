package com.example.demo.service

import com.example.demo.dto.AuthorDto
import com.example.demo.dto.AuthorWithBooksDto
import com.example.demo.dto.BookDto
import com.example.demo.dto.BookWithAuthorDto
import com.example.demo.model.Author
import com.example.demo.model.Book
import com.example.demo.repository.AuthorRepository
import com.example.demo.repository.BookRepository
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

    @WithSpan(value = "authorDTO")
    private fun convertToDto(author: Author, withBooks: Boolean): Any? {
        if (withBooks)
            return modelMapper.map(author, AuthorWithBooksDto::class.java);
        return modelMapper.map(author, AuthorDto::class.java);
    }

    @WithSpan(value = "bookDto")
    private fun convertToDto(book: Book, withAuthor: Boolean): Any? {
        if (withAuthor)
            return modelMapper.map(book, BookWithAuthorDto::class.java);
        return modelMapper.map(book, BookDto::class.java);
    }

    @WithSpan
    fun createBook(book: Book): BookWithAuthorDto? {
        val author = book.author;
        val auth = author?.firstName?.let { author.lastName?.let { it1 ->
            authorRepository.findByFirstNameAndLastName(it,
                it1
            )
        } }

        // Use Existing author OR create author
        if (auth != null) {
            book.author = auth
        } else {
            book.author = authorRepository.save(book.author!!);
        }

        val dto = convertToDto(bookRepository.save(book), true)
        if (dto is BookWithAuthorDto)
            return dto;

        return null;
    }

    @WithSpan
    fun getBookById(bookId: Long): Any? {
        val optionalBook = bookRepository.findById(bookId)
        if (optionalBook.isPresent) {
            return convertToDto(optionalBook.get(), false)
        }
        return null;
    }

    @WithSpan
    fun getAllBooks(): MutableList<BookWithAuthorDto> {
        val books = mutableListOf<BookWithAuthorDto>()
        for (book in bookRepository.findAll().toList()) {
            val dto = convertToDto(book, true)
            if (dto is BookWithAuthorDto)
                books.add(dto);
        }
        return books;
    }

    @WithSpan
    fun getAllAuthorsWithBookDetails(): MutableList<AuthorWithBooksDto> {
        val authors = authorRepository.findAll().toList();
        logger.info(authors.count().toString())
        //        return authors.stream().map { this::convertToDto }.collect(Collectors.toList())
        val authorDTOs = mutableListOf<AuthorWithBooksDto>()

        for (author in authors) {
            val dto = convertToDto(author, true)
            if (dto is AuthorWithBooksDto)
                authorDTOs.add(dto)
        }
        return authorDTOs;
    }

    @WithSpan
    fun getAuthorById(authorId: Long): Author {
        return authorRepository.findById(authorId).get()
    }
}