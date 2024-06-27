package com.example.demo.controller

import com.example.demo.model.Book
import com.example.demo.repository.AuthorRepository
import com.example.demo.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {

    @Autowired
    lateinit var bookRepository: BookRepository;

    @Autowired
    lateinit var authorRepository: AuthorRepository;

    @GetMapping("/")
    fun home() : String {
        return "Hello World";
    }

    @GetMapping("/books")
    fun books() : MutableIterable<Book> {
        return bookRepository.findAll();
    }

    @PostMapping("/book")
    fun createBook(@RequestBody book: Book): Any {
        if (book.author != null) {
            book.author = authorRepository.save(book.author!!);
        }
        return bookRepository.save(book);
    }
}
