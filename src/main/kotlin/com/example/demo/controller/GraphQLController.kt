package com.example.demo.controller

import com.example.demo.dto.BookDto
import com.example.demo.model.Author
import com.example.demo.model.Book
import com.example.demo.repository.AuthorRepository
import com.example.demo.repository.BookRepository
import com.example.demo.service.BookService
import io.opentelemetry.instrumentation.annotations.WithSpan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller


@Controller
class GraphQLController {
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

    @WithSpan
    @SchemaMapping
    fun author(book: Book): Author? {
        return book.author?.id?.let { authorRepository.findById(it).get() }
    }

    @WithSpan
    @SchemaMapping
    fun books(author: Author): List<Book>? {
        return author.id?.let { bookRepository.findByAuthorId(it) }
    }
}