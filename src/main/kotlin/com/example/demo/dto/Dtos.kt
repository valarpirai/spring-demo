package com.example.demo.dto

import java.io.Serializable

class AuthorDto: Serializable {
    var id: Long? = null
    lateinit var firstName: String
    lateinit var lastName: String
}

class AuthorWithBooksDto: Serializable {
    var id: Long? = null
    lateinit var firstName: String
    var lastName: String? = null
    var books: Set<BookDto>? = null
}

class BookDto: Serializable {
    var id: Long? = null
    lateinit var name: String
    var pageCount: Int? = null
}

class BookWithAuthorDto: Serializable {
    var id: Long? = null
    lateinit var name: String
    var pageCount: Int? = null
    var author: AuthorDto? = null
}
