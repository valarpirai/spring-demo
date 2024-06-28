package com.example.demo

//import com.example.demo.dto.AuthorDto
//import com.example.demo.model.Author
//import com.example.demo.model.Book
//import org.junit.Test
//import org.modelmapper.ModelMapper
//import kotlin.test.assertEquals
//import kotlin.test.assertNotNull
//import kotlin.test.assertNull
//
//class AuthorDtoTest {
//
//    val modelMapper = ModelMapper();
//
//    @Test
//    fun whenConvertToAuthorDto_withoutBooks() {
//        val author = Author()
//        author.id = 1
//        author.firstName = "Valar"
//        author.lastName = "Pirai"
//
//        var authorDto = modelMapper.map(author, AuthorDto::class.java)
//
//        assertEquals(author.id, authorDto.id)
//        assertEquals(author.firstName, authorDto.firstName)
//        assertEquals(author.lastName, authorDto.lastName)
//        assertNull(authorDto.books)
//    }
//
//    @Test
//    fun whenConvertToAuthorDto_withBooks() {
//        val author = Author()
//        author.id = 101
//        author.firstName = "Valar"
//        author.lastName = "Pirai"
//
//        val book = Book()
//        book.id = 100
//        book.author = author
//        book.name = "Hello Java"
//        book.pageCount = 100
//
//        author.books = setOf(book)
//
//        var authorDto = modelMapper.map(author, AuthorDto::class.java)
//
//        assertEquals(author.id, authorDto.id)
//        assertEquals(author.firstName, authorDto.firstName)
//        assertEquals(author.lastName, authorDto.lastName)
//        assertNotNull(authorDto.books)
//        assertEquals(1, authorDto.books!!.size)
//
//        val bookDto = authorDto.books!!.first()
//        assertEquals(book.id, bookDto.id )
//        assertEquals(book.name, bookDto.name)
//        assertEquals(book.pageCount, bookDto.pageCount)
//    }
//}