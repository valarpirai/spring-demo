package com.example.demo.repository

import com.example.demo.model.Author
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : CrudRepository<Author, Long> {
    fun save(author: Author): Author
    fun findByFirstNameAndLastName(firstName: String, lastName: String): Author?

    fun findByFirstNameAndLastNameIsNotNull(firstName: String): List<Author>

//    @Query(value = "SELECT a.id as id, a.firstName as firstName, a.lastName as lastName from Author a where first_name = :firstName AND last_name = :lastName")
//    fun findByFirstNameAndLastName(firstName: String, lastName: String): Author?
}
