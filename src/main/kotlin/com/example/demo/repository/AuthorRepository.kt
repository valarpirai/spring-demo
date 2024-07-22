package com.example.demo.repository

import com.example.demo.model.Author
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : CrudRepository<Author, Long> {
    fun save(author: Author): Author
    fun findByFirstNameAndLastName(firstName: String, lastName: String): Author?

//    @Query(value = "SELECT a.id, a.firstName, a.lastName from Author a where first_name = :firstName AND last_name = :lastName")
//    fun findByFirstNameAndLastName(firstName: String, lastName: String): Author?
}
