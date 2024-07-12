package com.example.demo

import com.example.demo.model.Book
import com.example.demo.service.BookService
import com.graphql.spring.boot.test.GraphQLTest
import com.graphql.spring.boot.test.GraphQLTestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.doReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean


@GraphQLTest
class BookQueryTest {

    @Autowired
    private val graphQLTestTemplate: GraphQLTestTemplate? = null

    @MockBean
    var bookServiceMock: BookService? = null

//    @Test
    fun getBooks() {
        val book: Book = Book()
        book.id = 1
        book.name = "test book"

        doReturn(book).`when`(bookServiceMock)?.getBooks(any(), any())

        val response = graphQLTestTemplate!!.postForResource("graphql/get-books.graphql")
        assertThat(response.isOk).isTrue()
        assertThat(response["$.data.createUser.id"]).isNotNull()
        assertThat(response["$.data.createUser.username"]).isEqualTo("TEST_USERNAME")
    }

}