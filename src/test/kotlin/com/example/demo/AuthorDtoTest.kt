package com.example.demo

import com.example.demo.dto.AuthorDto
import com.example.demo.model.Author
import org.junit.Test
import org.modelmapper.ModelMapper
import kotlin.test.assertEquals

class AuthorDtoTest {

    val modelMapper = ModelMapper();

    @Test
    fun whenConvertToAuthorDto_withoutBooks() {
        val author = Author()
        author.id = 1
        author.firstName = "Valar"
        author.lastName = "Pirai"

        var authorDto = modelMapper.map(author, AuthorDto::class.java)

        assertEquals(author.id, authorDto.id)
        assertEquals(author.firstName, authorDto.firstName)
        assertEquals(author.lastName, authorDto.lastName)

    }
}