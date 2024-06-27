package com.example.demo.dto

class AuthorDto {
    var id: Long? = null
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var books: Set<BookDto>
}

class BookDto {
    var id: Long? = null
    lateinit var name: String
    var pageCount: Long? = null
}

