package com.example.demo.dto

import lombok.Data

@Data
class AuthorDto {
    var id: Long? = null
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var books: Set<BookDto>
}

@Data
class BookDto {
    var id: Long? = null
    lateinit var name: String
    var pageCount: Long? = null
}

