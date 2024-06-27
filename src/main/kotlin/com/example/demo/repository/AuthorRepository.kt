package com.example.demo.repository

import com.example.demo.model.Author
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.Id

@Repository
interface AuthorRepository : CrudRepository<Author, Id> {
    abstract fun save(author: Author): Author
}