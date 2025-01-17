package com.example.demo.controller

import com.example.demo.dto.BookWithAuthorDto
import com.example.demo.model.Author
import com.example.demo.model.Book
import com.example.demo.repository.AuthorRepository
import com.example.demo.repository.BookRepository
import com.example.demo.service.BookService
import io.opentelemetry.instrumentation.annotations.WithSpan
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller


@Controller
class GraphQLController {
    private val logger: Logger = LoggerFactory.getLogger(GraphQLController::class.java)

    @Autowired
    lateinit var bookService: BookService

    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var authorRepository: AuthorRepository

    @QueryMapping
    fun bookById(@Argument id: Long): Book {
        return bookRepository.findById(id).get();
    }

    @QueryMapping
    fun authorById(@Argument id: Long): Author {
        return authorRepository.findById(id).get()
    }

    @QueryMapping
    fun getBooks(@Argument page: Int, @Argument pageSize: Int): Map<String, Any> {
        logger.info("$page -> $pageSize");
        val pageInfo = mapOf("page" to page, "pageSize" to pageSize, "hasNextPage" to false);
        val responseObj = mapOf("books" to bookService.getBooks(page, pageSize),
            "pageInfo" to pageInfo);

        return responseObj;
    }

    @MutationMapping
    fun createBook(@Argument input: Book): BookWithAuthorDto? {
        return bookService.createBook(input);
    }

//    @WithSpan
//    @SchemaMapping
//    fun author(book: Book): Author? {
//        return book.author?.id?.let { authorRepository.findById(it).get() }
//    }

//    @WithSpan
//    @SchemaMapping
//    fun books(author: Author): List<Book>? {
//        return author.id?.let { bookRepository.findByAuthorId(it) }
//    }
}