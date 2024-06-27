package com.example.demo.repository

import com.example.demo.model.Author
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : CrudRepository<Author, Long> {
    abstract fun save(author: Author): Author
    abstract fun findByFirstNameAndLastName(firstName: String, lastName: String): Author?
}