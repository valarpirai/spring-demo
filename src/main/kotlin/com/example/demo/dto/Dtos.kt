package com.example.demo.dto

class AuthorDto {
    var id: Long? = null
    lateinit var firstName: String
    lateinit var lastName: String
}

class AuthorWithBooksDto {
    var id: Long? = null
    lateinit var firstName: String
    lateinit var lastName: String
    var books: Set<BookDto>? = null
}

class BookDto {
    var id: Long? = null
    lateinit var name: String
    var pageCount: Int? = null
}

class BookWithAuthorDto {
    var id: Long? = null
    lateinit var name: String
    var pageCount: Int? = null
    var author: AuthorDto? = null
}
